package com.BugTracking.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.BugTracking.codes.DataBase;

public class cTask {

	private int ID;
	private String Name;
	private String Description;
	private int Priority;
	private Timestamp DateCreate;
	private Timestamp DateModify;
//	private cProject Project;
	private TaskState State; // Новая, В работе, Закрыта

	public static boolean Add(String name, String description, int priority, Timestamp dateCreate, Timestamp dateModify,
			int idProject) {
		String query = MessageFormat.format(
				"insert into Task (Name,Description,Priority,DateCreate,DateModify,idProject,idState)"
						+ "values (''{0}'',''{1}'',{2},to_timestamp(''{3}'', ''DD.MM.YYYY HH24:MI:SS''),"
						+ "to_timestamp(''{4}'', ''DD.MM.YYYY HH24:MI:SS''),{5},0)", // Новая
				name, description, priority, (new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(dateCreate),
				(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(dateModify), idProject);
		return DataBase.Insert(query);
	}

	public static boolean Update(int id, String name, String description, int priority, Timestamp dateModify, int state) throws SQLException {
		String checkState=MessageFormat.format("Select idState from Task where id={0}",id);
		ResultSet result = DataBase.Select(checkState);
		result.next();
		int curState=result.getInt("idState");
		if (curState==2) { //2 - CLOSED
			return false;
		}
		String query = MessageFormat.format(
				"update Task set Name=''{0}'', Description=''{1}'', Priority={2}, "
						+ "DateModify=to_timestamp(''{3}'', ''DD.MM.YYYY HH24:MI:SS''), idState={4} where id={5}",
				name, description, priority, (new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(dateModify), state, id);
		return DataBase.Update(query);
	}

	static public List<cTask> getTasks(String idProject, String orderField, String way,
			String dateFrom, String dateTo, int priorityFrom,
										int priorityTo, String states,
										int page, int count) throws SQLException {
		// Задачи проекта
		List<cTask> tasks = new ArrayList<cTask>();
		String query = MessageFormat.format("select T.*,TS.Name2 from Task T "
				+ "left join TaskState TS on TS.ID=T.idState "
				+ "where T.IdProject={0} "
				+ "and T.DateCreate between to_timestamp(''{1}'', ''DD.MM.YYYY HH24:MI:SS'') "
									+ "and to_timestamp(''{2}'', ''DD.MM.YYYY HH24:MI:SS'') "
				+ "and T.Priority between {3} and {4} "
				+ "and T.idState in ({5}) "
				+ "order by {6} {7} "
				+ "limit {8} offset {9}",
				idProject,
				(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(new Timestamp(Long.parseLong(dateFrom))),
				(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(new Timestamp(Long.parseLong(dateTo))),
				priorityFrom,priorityTo,states,orderField,way,
				count,count*(page-1));
		ResultSet result = DataBase.Select(query);

		// Записываем результаты
		while (result.next()) {
			cTask task = new cTask();
			task.ID = result.getInt("ID");
			task.Name = result.getString("Name");
			task.Description = result.getString("Description");
			task.DateCreate = result.getTimestamp("DateCreate");
			task.DateModify = result.getTimestamp("DateModify");
			task.Priority = result.getInt("Priority");
			task.State = TaskState.valueOf(result.getString("Name2"));
//			task.Project.setId(id); = result.getDate("DateModify");
			tasks.add(task);

//			System.out.println("Номер в выборке #" + result.getRow() + "\t Номер в базе #" + result.getInt("ID") + "\t"
//					+ result.getString("Name"));
		}
		return tasks;
	}

	public static boolean Remove(int id) {
		String query = MessageFormat.format("delete from Task where Id={0}",id);
		return DataBase.Delete(query);
	}
	
	public int getId() {
		return ID;
	}

	public void setId(int id) {
		ID = id;
	}

	public TaskState getState() {
		return State;
	}

	public void setState(TaskState state) {
		State = state;
	}

	public int getPriority() {
		return Priority;
	}

	public void setPriority(int priority) {
		Priority = priority;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Timestamp getDateCreate() {
		return DateCreate;
	}

	public void setDateCreate(Timestamp dateCreate) {
		DateCreate = dateCreate;
	}

	public Timestamp getDateModify() {
		return DateModify;
	}

	public void setDateModify(Timestamp dateModify) {
		DateModify = dateModify;
	}
}

//Состояния задач
enum TaskState {
	NEW, INWORK, CLOSED
};

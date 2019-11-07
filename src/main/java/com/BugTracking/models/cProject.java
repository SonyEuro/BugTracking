package com.BugTracking.models;

import com.BugTracking.codes.DataBase;
import java.util.*;
import java.sql.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

public class cProject {

	private int ID;
	private String Name;
	private String Description;
	private Timestamp DateCreate;
	private Timestamp DateModify;
//	private cTask[] Tasks;
	
	public cProject(String name, String description, Timestamp dateCreate, Timestamp dateModify) {
		Name = name;
		Description = description;
		DateCreate = dateCreate;
		DateModify = dateModify;
	}
	
	public cProject() {
	}
	
	public static boolean Remove(int id) {
		String query = MessageFormat.format("delete from Project where Id={0}",id);
		return DataBase.Delete(query);
	}

	public static boolean Add(String name, String description, Timestamp dateCreate, Timestamp dateModify) {
		String query = MessageFormat.format(
				"insert into Project (Name,Description,DateCreate,DateModify)"
				+ "values (''{0}'',''{1}'',to_timestamp(''{2}'', ''DD.MM.YYYY HH24:MI:SS''),to_timestamp(''{3}'', ''DD.MM.YYYY HH24:MI:SS''))", 
				name,description,(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(dateCreate),
				(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(dateModify));
		return DataBase.Insert(query);
	}
	
	public static boolean Update(int id, String name, String description, Timestamp dateModify) {
		String query = MessageFormat.format(
				"update Project set "
				+ "Name=''{0}'', "
				+ "Description=''{1}'', "
				+ "DateModify=to_timestamp(''{2}'', ''DD.MM.YYYY HH24:MI:SS'') "
				+ "where id={3}",
				name,description,(new SimpleDateFormat("dd.MM.YYYY hh:mm:ss")).format(dateModify),id);
		return DataBase.Update(query);
	}
	
	static public List<cProject> getProjects(int page, int count) throws SQLException {
		// Проекты
		List<cProject> prjcts = new ArrayList<cProject>();
		String query = MessageFormat.format("select * from Project order by DateCreate "
				+ "limit {0} offset {1}",count,count*(page-1));
		ResultSet result = DataBase.Select(query);

		// Записываем результаты
		while (result.next()) {
			cProject prjct = new cProject();
			prjct.ID = result.getInt("ID");
			prjct.Name = result.getString("Name");
			prjct.Description = result.getString("Description");
			prjct.DateCreate = result.getTimestamp("DateCreate");
			prjct.DateModify = result.getTimestamp("DateModify");
			prjcts.add(prjct);

//			System.out.println("Номер в выборке #" + result.getRow() +
//					"\t Номер в базе #" + result.getInt("ID") + "\t"
//					+ result.getString("Name"));
		}
		return prjcts;
	}
	
	static public List<cProject> getProjects(int id) throws SQLException {
		// Проекты
		List<cProject> prjcts = new ArrayList<cProject>();
		String query = "select * from Project where id="+Integer.toString(id);
		ResultSet result = DataBase.Select(query);

		// Записываем результаты
		while (result.next()) {
			cProject prjct = new cProject();
			prjct.ID = result.getInt("ID");
			prjct.Name = result.getString("Name");
			prjct.Description = result.getString("Description");
			prjct.DateCreate = result.getTimestamp("DateCreate");
			prjct.DateModify = result.getTimestamp("DateModify");
			prjcts.add(prjct);

			System.out.println("Номер в выборке #" + result.getRow() +
					"\t Номер в базе #" + result.getInt("ID") + "\t"
					+ result.getString("Name"));
		}
		return prjcts;
	}

	public int getId() {
		return ID;
	}

	public void setId(int id) {
		ID = id;
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

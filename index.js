var projects;	//проекты
var tasks;		//задачи
var idSelProj; //id выбранного проекта
var idSelTask; //id выбранной задачи

function SelectTableProject() {
    //Обработка выбора проекта в таблице
    $("#tblProjects tr:not(#thead1)").click(function () {
        $(this).addClass('selected').siblings().removeClass('selected');
        idSelProj = $(this)[0].children[0].innerText;
        LoadTasks();
        $('#btnDeleteProject')[0].disabled = false;
        $('#btnDeleteTask')[0].disabled = true;
    });
}

function SelectTableTask() {
    //Обработка выбора задачи в таблице
    $("#tblTasks tr:not(#thead2)").click(function () {
        $(this).addClass('selected').siblings().removeClass('selected');
        idSelTask = $(this)[0].children[0].innerText;
        $('#btnDeleteTask')[0].disabled = false;
    });
    //Обработка выбора задачи в таблице
    $("#tblTasks td.ordered").click(function () {
        $(this).addClass('orderBy').siblings().removeClass('orderBy');
        taskOrderField = $(this)[0].innerHTML;
        if (taskOrderWay == 'desc')
            taskOrderWay = 'asc';
        else
            taskOrderWay = 'desc';
        LoadTasks();
    });
}

function Load() {
    $('#btnDeleteTask')[0].disabled = true;
    $('#btnDeleteProject')[0].disabled = true;
    LoadProjects();
}

//Загрузить список проектов
function LoadProjects(page) {
    //Отправляем запрос на получение списка в формате JSON
    $.ajax({
        //url: 'http://127.0.0.1:8080/projects',       
        url: 'http://127.0.0.1:8080/projects',
        type: 'GET',
        data: {
            page: pageProject,
            count: 5
        },
        //dataType: 'application/json; charset=utf-8',
        //При успешном возврате заполняем Список темами
        success: function (data) {
            projects = JSON.parse(data);
            $('#tblProjects')[0].innerHTML =
                `<tr id='thead1'>
                <td>
                    ID
                </td>
                <td>
                    Name
                </td>
                <td>
                    Description
                </td>
                <td>
                    DateCreate
                </td>
                <td>
                    DateModify
                </td>
            </tr>`;
            for (var i = 0; i < projects.length; i++) {
                var elm = document.createElement('tr');
                elm.innerHTML = "<td>" + projects[i].id + "</td>";
                elm.innerHTML += "<td>" + projects[i].name + "</td>";
                elm.innerHTML += "<td>" + projects[i].description + "</td>";
                elm.innerHTML += "<td>" + new Date(projects[i].dateCreate).toLocaleString('ru-RU') + "</td>";
                elm.innerHTML += "<td>" + new Date(projects[i].dateModify).toLocaleString('ru-RU') + "</td>";
                $('#tblProjects')[0].appendChild(elm);
            }
            $('#lPagesProject')[0].innerHTML=pageProject;//+" из "+(Math.floor(projects.length/5)+1);
            SelectTableProject();
        },
        //В случае ошибки
        error: function () {
            alert('Ошибка при работе с сервером!');
        }
    });
}

function PrevPageTask() {
    pageTask--;
    $('#lPagesTask')[0].innerHTML=pageTask;//+" из "+(Math.floor(tasks.length/5)+1);
    LoadTasks();
}

function NextPageTask() {
    pageTask++;
    $('#lPagesTask')[0].innerHTML=pageTask;//+" из "+(Math.floor(tasks.length/5)+1);
    LoadTasks();
}

function PrevPageProject() {
    pageProject--;
    $('#lPagesProject')[0].innerHTML=pageProject;//+" из "+(Math.floor(projects.length/5)+1);
    LoadProjects();
}

function NextPageProject() {
    pageProject++;
    $('#lPagesProject')[0].innerHTML=pageProject;//+" из "+(Math.floor(projects.length/5)+1);
    LoadProjects();
}

var taskOrderField = 'DateCreate';
var taskOrderWay = 'asc';
var pageProject = 1;
var pageTask = 1;
//Загрузить список задач
function LoadTasks() {
    var states = '';
    $('.states:checked').each((i, el) => states += el.value + ',');
    states = states.substring(0, states.length - 1);
    //Отправляем запрос на получение списка в формате JSON
    $.ajax({
        //url: 'http://127.0.0.1:8080/projects',       
        url: 'http://127.0.0.1:8080/projects/' + idSelProj + '/tasks',
        type: 'GET',
        data: {
            orderBy: taskOrderField,
            way: taskOrderWay,
            dateFrom: new Date($('#dateFrom').val()).getTime(),
            dateTo: new Date($('#dateTo').val()).getTime(),
            priorityFrom: $('#priorityFrom').val(),
            priorityTo: $('#priorityTo').val(),
            states: states,
            page: pageTask,
            count: 10
        },
        //dataType: 'application/json; charset=utf-8',
        //При успешном возврате заполняем Список темами
        success: function (data) {
            tasks = JSON.parse(data);
            $('#tblTasks')[0].innerHTML =
                `<tr id='thead2'>
                <td>
                    ID
                </td>
                <td>
                    Name
                </td>
                <td>
                    Description
                </td>
                <td class="ordered">
                    Priority
                </td>
                <td class="ordered">
                    DateCreate
                </td>
                <td class="ordered">
                    DateModify
                </td>
                <td>
                    State
                </td>
            </tr>`;
            for (var i = 0; i < tasks.length; i++) {
                var elm = document.createElement('tr');
                elm.innerHTML = "<td>" + tasks[i].id + "</td>";
                elm.innerHTML += "<td>" + tasks[i].name + "</td>";
                elm.innerHTML += "<td>" + tasks[i].description + "</td>";
                elm.innerHTML += "<td>" + tasks[i].priority + "</td>";
                elm.innerHTML += "<td>" + new Date(tasks[i].dateCreate).toLocaleString('ru-RU') + "</td>";
                elm.innerHTML += "<td>" + new Date(tasks[i].dateModify).toLocaleString('ru-RU') + "</td>";
                elm.innerHTML += "<td>" + tasks[i].state + "</td>";
                $('#tblTasks')[0].appendChild(elm);
            }
            $('#lPagesTask')[0].innerHTML=pageTask;//+" из "+(Math.floor(tasks.length/5)+1);
            SelectTableTask();
        },
        //В случае ошибки
        error: function () {
            alert('Ошибка при работе с сервером!');
        }
    });
}

//Закрыть модальное окно
function CloseModal() {
    $('.modal').modal('hide');
}

function StartAddProject() {
    $('#modalAddProject').off('show.bs.modal');//удалим все обработчики событий
    $('#modalAddProject').on('show.bs.modal', function () {
        $('#tbProjectName').val('');
        $('#taProjectDescription').val('');
        $('#btnSaveProject')[0].onclick = AddProject;
    });
    //Открыть модальное окно
    $('#modalAddProject').modal({
        keyboard: true,
        backdrop: 'static' //Не закрывать окно при нажатии за окном 
    });
}

function StartEditProject() {
    $('#modalAddProject').off('show.bs.modal');//удалим все обработчики событий
    $('#modalAddProject').on('show.bs.modal', function () {
        var idproj = projects.findIndex(p => p.id == idSelProj);
        $('#tbProjectName').val(projects[idproj].name);
        $('#taProjectDescription').val(projects[idproj].description);
        $('#btnSaveProject')[0].onclick = EditProject;
    });
    //Открыть модальное окно
    $('#modalAddProject').modal({
        keyboard: true,
        backdrop: 'static' //Не закрывать окно при нажатии за окном 
    });
}

function StartAddTask() {
    $('#modalAddTask').off('show.bs.modal');//удалим все обработчики событий
    $('#modalAddTask').on('show.bs.modal', function () {
        $('#tbTaskName').val('');
        $('#taTaskDescription').val('');
        $('#tbTaskPriority').val('');
        $('#slctTaskState')[0].selectedIndex = 0;
        $('#btnSaveTask')[0].onclick = AddTask;
    });
    //Открыть модальное окно
    $('#modalAddTask').modal({
        keyboard: true,
        backdrop: 'static' //Не закрывать окно при нажатии за окном 
    });
}

function StartEditTask() {
    $('#modalAddTask').off('show.bs.modal');//удалим все обработчики событий
    $('#modalAddTask').on('show.bs.modal', function () {
        $('#btnSaveTask')[0].onclick = EditTask;
        var idtask = tasks.findIndex(p => p.id == idSelTask);
        $('#tbTaskName').val(tasks[idtask].name);
        $('#taTaskDescription').val(tasks[idtask].description);
        $('#tbTaskPriority').val(tasks[idtask].priority);
        $('#slctTaskState')[0].selectedIndex = Array.from($('#slctTaskState')[0].options).findIndex(p => p.value == tasks[idtask].state);
        if (tasks[idtask].state == 2) //Закрыта
        {
            $('#tbTaskName').disabled = true;
            $('#taTaskDescription').disabled = true;
            $('#tbTaskPriority').disabled = true;
            $('#slctTaskState')[0].disabled = true;
        }
    });
    //Открыть модальное окно
    $('#modalAddTask').modal({
        keyboard: true,
        backdrop: 'static' //Не закрывать окно при нажатии за окном 
    });
}

function AddProject() {
    //Отправляем запрос на удаление из базы
    $.ajax({
        url: 'http://127.0.0.1:8080/projects',//Метод в контроллере       
        type: 'POST',
        data: {
            name: $('#tbProjectName').val(),
            description: $('#taProjectDescription').val(),
            dateCreate: $.now(),
            dateModify: $.now()
        },
        //При успешном возврате Обновляем список
        success: function () {
            Load();
            CloseModal();
        },
        //В случае ошибки
        error: function () {//TODO: Коды ошибок
            alert('Ошибка при работе с сервером!');
        }
    });
}

function EditProject() {
    //Отправляем запрос на удаление из базы
    $.ajax({
        url: 'http://127.0.0.1:8080/projects',//Метод в контроллере       
        type: 'PUT',
        data: {
            id: idSelProj,
            name: $('#tbProjectName').val(),
            description: $('#taProjectDescription').val(),
            dateModify: $.now()
        },
        //При успешном возврате Обновляем список
        success: function () {
            Load();
            CloseModal();
        },
        //В случае ошибки
        error: function () {//TODO: Коды ошибок
            alert('Ошибка при работе с сервером!');
        }
    });
}

function DeleteProject() {
    //Отправляем запрос на удаление из базы
    $.ajax({
        url: 'http://127.0.0.1:8080/projects/' + idSelProj,//Метод в контроллере       
        type: 'DELETE',
        //  data: { id: idSelProj },
        //При успешном возврате Обновляем список
        success: function () {
            Load();
            CloseModal();
        },
        //В случае ошибки
        error: function () {//TODO: Коды ошибок
            alert('Ошибка при работе с сервером!');
        }
    });
}

function AddTask() {
    //Отправляем запрос на удаление из базы
    $.ajax({
        url: 'http://127.0.0.1:8080/projects/' + idSelProj + '/tasks',//Метод в контроллере       
        type: 'POST',
        data: {
            name: $('#tbTaskName').val(),
            description: $('#taTaskDescription').val(),
            priority: $('#tbTaskPriority').val(),
            dateCreate: $.now(),
            dateModify: $.now(),
            idProject: idSelProj
        },
        //При успешном возврате Обновляем список
        success: function () {
            LoadTasks();
            CloseModal();
        },
        //В случае ошибки
        error: function () {//TODO: Коды ошибок
            alert('Ошибка при работе с сервером!');
        }
    });
}

function EditTask() {
    //Отправляем запрос на удаление из базы
    $.ajax({
        url: 'http://127.0.0.1:8080/projects/' + idSelProj + '/tasks',//Метод в контроллере       
        type: 'PUT',
        data: {
            id: idSelTask,
            name: $('#tbTaskName').val(),
            description: $('#taTaskDescription').val(),
            priority: $('#tbTaskPriority').val(),
            dateModify: $.now(),
            idState: $('#slctTaskState')[0].selectedOptions[0].id
        },
        //При успешном возврате Обновляем список
        success: function () {
            LoadTasks();
            CloseModal();
        },
        //В случае ошибки
        error: function () {//TODO: Коды ошибок
            alert('Ошибка при работе с сервером!');
        }
    });
}

function DeleteTask() {
    //Отправляем запрос на удаление из базы
    $.ajax({
        url: 'http://127.0.0.1:8080/projects/' + idSelProj + '/tasks/' + idSelTask,//Метод в контроллере       
        type: 'DELETE',
        //data: { idTask: idSelTask },
        //При успешном возврате Обновляем список
        success: function () {
            LoadTasks();
            CloseModal();
        },
        //В случае ошибки
        error: function () {//TODO: Коды ошибок
            alert('Ошибка при работе с сервером!');
        }
    });
}
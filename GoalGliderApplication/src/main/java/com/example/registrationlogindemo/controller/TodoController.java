package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Todo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TodoController {

    @RequestMapping("listTodos")
    public String listAllTodos() {


//        String username = getLoggedInUsername(model);
//        List<Todo> todos = todoRepository.findByUsername(username);
//        model.addAttribute("todos", todos);
//         System.out.println("Hello Kartik");





        return "listtodos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(ModelMap model) {
//        String username = getLoggedInUsername(model);
////		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
//        Todo todo = new Todo("defaultDes",LocalDate.now(),false,);
//        model.put("todo", todo);
        return "todo";
    }



}

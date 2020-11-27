import { Component, OnInit } from '@angular/core';
import { Todo } from '../todo';
import { TodoService } from '../todo.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.scss']
})

export class TodosComponent implements OnInit {

  selectedTodo: Todo;

  todos: Todo[];

  constructor(private todoService: TodoService, private messageService: 
             MessageService) { }
  
  onSelect(todo: Todo): void {
    this.selectedTodo = todo;
    this.messageService.add('TodosComponent: Selected todo id=${todo.id}')
  }

  getTodos(): void {
     this.todoService.getTodos().subscribe(todos => this.todos = todos);
  }

  ngOnInit(): void {
    this.getTodos();
  }
}

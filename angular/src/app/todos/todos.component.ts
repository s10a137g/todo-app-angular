import { Component, OnInit } from '@angular/core';
import { Todo } from '../todo';
import { TodoService } from '../todo.service';
import { MessageService } from '../message.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrls: ['./todos.component.scss']
})

export class TodosComponent implements OnInit {
  title = ""
  selectedTodo: Todo;

  todos: Todo[];

  constructor(private todoService: TodoService, 
              private messageService: MessageService,
              private router: Router,
             ){ }

  ngOnInit(): void {
    this.title = 'TODO List'
    this.getTodos();
  }

  
  onSelect(todo: Todo): void {
    this.selectedTodo = todo;
    this.messageService.add('TodosComponent: Selected todo id=${todo.id}')
  }

  getTodos(): void {
     this.todoService.getTodos().subscribe(todos => this.todos = todos);
  }

  onTodoDeleteSubmit(id: number): void {
    this.todoService.deleteTodos(id).subscribe(() => this.router.navigate(['/todos/list']))
  }
}

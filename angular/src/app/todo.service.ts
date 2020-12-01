import { Injectable } from '@angular/core';
import { Todo } from './todo';
import { Observable } from 'rxjs';
import { MessageService } from './message.service'
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class TodoService {
  
  constructor(private http: HttpClient,
              private messageService: MessageService) { }

  private todoGetUrl = 'http://localhost:9000/api/todo/get';
  private todosGetUrl = 'http://localhost:9000/api/todos/get';
  private todosInsertUrl = 'http://localhost:9000/api/todos/insert';
  private todosUpdateUrl = 'http://localhost:9000/api/todos/update';
  private todosDeleteUrl = 'http://localhost:9000/api/todos/delete';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getTodo(id: number): Observable<Todo> {
    this.messageService.add("TodoService: ferched todo")
    const url = `${this.todoGetUrl}/${id}`;
    return this.http.get<Todo>(url)  
  }

  getTodos(): Observable<Todo[]> {
    this.messageService.add("TodoService: ferched todos")
    return this.http.get<Todo[]>(this.todosGetUrl)  
  }

  insertTodos(todo: Todo): Observable<Todo[]> {
    return this.http.post<Todo[]>(this.todosInsertUrl, todo, this.httpOptions)  
  }

  updateTodos(todo: Todo): Observable<Todo[]> {
    return this.http.post<Todo[]>(this.todosUpdateUrl, todo, this.httpOptions)  
  }

  deleteTodos(id: number): Observable<Todo[]> {
    const url = `${this.todosDeleteUrl}/${id}`;
    return this.http.delete<Todo[]>(url)  
  }

}

import { Injectable } from '@angular/core';
import { Todo } from './todo';
import { TODOS } from './mock-todos';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service'
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class TodoService {
  
  constructor(private http: HttpClient,
              private messageService: MessageService) { }

  private todosGetUrl = 'http://localhost:9000/api/todos/get';
  private todosInsertUrl = 'http://localhost:9000/api/todos/insert';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getTodos(): Observable<Todo[]> {
    this.messageService.add("TodoService: ferched todos")
    return this.http.get<Todo[]>(this.todosGetUrl)  
  }

  insertTodos(todo: Todo): Observable<Todo[]> {
    return this.http.post<Todo[]>(this.todosInsertUrl, todo, this.httpOptions)  
  }

  private log(message: string) {
    this.messageService.add(`TodoService: ${message}`);
  }
}

import { Injectable } from '@angular/core';
import { Todo } from './todo';
import { TODOS } from './mock-todos';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service'
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})

export class TodoService {
  
  constructor(private http: HttpClient,
              private messageService: MessageService) { }

  private todosUrl = 'http://localhost:9000/api/todos/get';

  getTodos(): Observable<Todo[]> {
    this.messageService.add("TodoService: ferched todos")
    return this.http.get<Todo[]>(this.todosUrl)  
    // return this.http.get<Todo[]>(this.todosUrl)
    //return of(TODOS)
  }

  private log(message: string) {
    this.messageService.add(`TodoService: ${message}`);
  }
}

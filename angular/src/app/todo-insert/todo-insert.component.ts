import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { TodoService } from '../todo.service'

@Component({
  selector: 'app-todo-insert',
  templateUrl: './todo-insert.component.html',
  styleUrls: ['./todo-insert.component.scss']
})
export class TodoInsertComponent implements OnInit {

  insertForm

  constructor(
    private todoService: TodoService,
    private formBuilder: FormBuilder,
  ) {
    this.insertForm = this.formBuilder.group({
      title: '',
      body: '',
      status: '',
      category: '',
    })
  }
  
  ngOnInit(): void {
  }
  
  onSubmit(tododata) {
    console.warn('insert form')
  }
}

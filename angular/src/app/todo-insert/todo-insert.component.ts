import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { Todo} from '../todo'
import { TodoService } from '../todo.service'
import { CategoryService } from '../category.service'
import { Category } from '../category'

@Component({
  selector: 'app-todo-insert',
  templateUrl: './todo-insert.component.html',
  styleUrls: ['./todo-insert.component.scss']
})
export class TodoInsertComponent implements OnInit {

  insertForm;
  categories: Category[];

  constructor(
    private todoService: TodoService,
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private router: Router,
  ) {
    this.insertForm = this.formBuilder.group({
      title: '',
      body: '',
      category: '',
    })
  }
  
  ngOnInit(): void {
    this.categoryService.getCategories().subscribe(categories => this.categories = categories)
  }
  
  onTodoInsertSubmit(tododata: Todo): void {
    this.todoService.insertTodos(tododata).subscribe(() => this.router.navigate(['/todos/list']))
  }
}

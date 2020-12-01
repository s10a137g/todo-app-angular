import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router'; 
import { Todo} from '../todo'
import { TodoService } from '../todo.service'
import { CategoryService } from '../category.service'
import { Category } from '../category'
import { Observable } from 'rxjs';


@Component({
  selector: 'app-todo-update',
  templateUrl: './todo-update.component.html',
  styleUrls: ['./todo-update.component.scss']
})
export class TodoUpdateComponent implements OnInit {
  todo: Todo; 
  updateForm;
  categories: Category[];

  constructor(
    private todoService: TodoService,
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
  ){
    this.updateForm = this.formBuilder.group({
      id:  "",
      title: "",
      body: "",
      status: "",
      category: ""
    })
  }

  ngOnInit(): void {
    this.getTodo().subscribe(todo => this.todo = todo)// this.updateForm.controls['title'].setValue(todo.title));
    this.categoryService.getCategories().subscribe(categories => this.categories = categories)
  }

  onTodoUpdateSubmit(tododata: Todo): void {
    this.todoService.updateTodos(tododata).subscribe(() => this.router.navigate(['/todos/list']))
  }

  getTodo(): Observable<Todo>{
    const id = +this.route.snapshot.paramMap.get('id');
    return this.todoService.getTodo(id)// .subscribe(todo => this.todo = todo)
  }
}

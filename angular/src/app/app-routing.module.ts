import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TopComponent } from './top/top.component';
import { TodosComponent } from './todos/todos.component';
import { TodoInsertComponent } from './todo-insert/todo-insert.component';
import { TodoUpdateComponent } from './todo-update/todo-update.component';
import { CategoriesComponent } from './categories/categories.component';

const routes: Routes = [
  { path: '', component: TopComponent },
  { path: 'todos/list', component: TodosComponent },
  { path: 'todos/insert', component: TodoInsertComponent },
  { path: 'todos/update/:id', component: TodoUpdateComponent },
  { path: 'todos/delete/:id', component: TodosComponent },
  { path: 'categories/list', component: CategoriesComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TopComponent } from './top/top.component';
import { TodosComponent } from './todos/todos.component';
import { TodoInsertComponent } from './todo-insert/todo-insert.component';
import { CategoriesComponent } from './categories/categories.component';

const routes: Routes = [
  { path: '', component: TopComponent },
  { path: 'todos/list', component: TodosComponent },
  { path: 'todos/insert', component: TodoInsertComponent },
  { path: 'categories/list', component: CategoriesComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

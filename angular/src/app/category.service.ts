import { Injectable } from '@angular/core';
import { Category } from './category'
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class CategoryService {

  constructor(private http: HttpClient) { }

  private categoryGetUrl = 'http://localhost:9000/api/category/get';
  private categoriesGetUrl = 'http://localhost:9000/api/categories/get';
  private categoryDeleteUrl = 'http://localhost:9000/api/category/delete';
  private categoryInsertUrl = 'http://localhost:9000/api/category/insert';
  private categoryUpdateUrl = 'http://localhost:9000/api/category/update';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getCategory(id: number): Observable<Category> {
    const url = `${this.categoryGetUrl}/${id}`;
    return this.http.get<Category>(url)  
  } 
  
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoriesGetUrl)  
  } 
  
  insertCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.categoryInsertUrl, category, this.httpOptions)  
  }

  updateCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.categoryUpdateUrl, category, this.httpOptions)  
  }

  deleteCategory(id: number): Observable<Category> {
    const url = `${this.categoryDeleteUrl}/${id}`;
    return this.http.delete<Category>(url)  
  }
}

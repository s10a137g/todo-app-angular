import { Component, OnInit } from '@angular/core';
import { Category } from '../category';
import { CategoryService } from '../category.service';
import { ActivatedRoute } from '@angular/router'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {
  categories: Category[]
  
  constructor(private categoryService: CategoryService,
              private router: Router,) { }
  
  getCategories(): void {
     this.categoryService.getCategories().subscribe(categories => this.categories = categories);
  }

  ngOnInit(): void {
    this.getCategories();
  }
  onCategoryDeleteSubmit(id: number): void {
    console.log(id)
    this.categoryService.deleteCategory(id).subscribe(() => this.router.navigate(['/categories/list']))
  }

}

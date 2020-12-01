import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import {ActivatedRoute} from '@angular/router';

import { CategoryService } from '../category.service'
import { Category } from '../category'

@Component({
  selector: 'app-category-update',
  templateUrl: './category-update.component.html',
  styleUrls: ['./category-update.component.scss']
})
export class CategoryUpdateComponent implements OnInit {

  category: Category
  constructor(
    private categoryService: CategoryService,
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  updateForm = this.formBuilder.group({
    id: '', 
    name: '',
    slug: '',
    color: ''
  })

  ngOnInit(): void {
    this.getCategory().subscribe(category => {
      this.category = category;
      this.updateForm.patchValue({
        id: this.category.id,
        name: this.category.name,
        slug: this.category.slug,
        color: String(this.category.colorCode),
      })
    });

  }

  onCategoryUpdateSubmit(categorydata: Category): void {
    this.categoryService.updateCategory(categorydata).subscribe(() => this.router.navigate(['/categories/list']))
  }

  getCategory(): Observable<Category> {
    const id = +this.route.snapshot.paramMap.get('id');
    return this.categoryService.getCategory(id)
  }
}

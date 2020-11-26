import {Component} from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'todo-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  content = 'はじめの第一歩';
  
  constructor(private http: HttpClient) {}
  
  onClick() {
  this.http.get('http://localhost:9000/api/get').subscribe(
    (res: string) => {
      this.content = res;
    }
  );
}
}

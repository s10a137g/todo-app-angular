import {Component} from '@angular/core';

@Component({
  selector: 'todo-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  content = 'TOP';
  
  constructor  () {}
}

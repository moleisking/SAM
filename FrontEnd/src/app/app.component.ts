import { Component } from "@angular/core";

import { AuthService } from "./services/auth";

@Component({
  selector: "app-root",
  templateUrl: "../views/app-root.html",
  styleUrls: ["../styles/app-root.css"],
})

export class AppComponent {

  constructor(private authService: AuthService) { }

  isDisabledIfLoggedIn() {
    return this.authService.isLoggedIn();
  }
}

import { Component } from "@angular/core";
import { ROUTER_DIRECTIVES } from "@angular/router";

import { Home } from "./components/home";
import { Register } from "./components/register";
import { Login } from "./components/login";
import { About } from "./components/about";
import { Dashboard } from "./components/dashboard";
import { ForgottenPassword } from "./components/forgottenpassword";
import { TermsConditions } from "./components/termsconditions";
import { ContactUs } from "./components/contactus";
import { Categories } from "./components/categories";
import { Profile } from "./components/profile";

import { AuthService } from "./services/auth";

@Component({
  selector: "app-root",
  templateUrl: "/views/app-root.html",
  styleUrls: ["/styles/app-root.css"],
  directives: [ROUTER_DIRECTIVES],
  precompile: [Home, Register, Login, About, Dashboard, ForgottenPassword, TermsConditions, ContactUs, Categories, Profile]
  // ,styles: [`
  //     a.disabled, a.disabled:visited {
  //       pointer-events: none;
  //       cursor: none;
  //       color: gray;
  //     }`
  // ]
})

export class AppComponent {

  constructor(private authService: AuthService) { }

  isDisabledIfLoggedIn() {
    return this.authService.isLoggedIn();
  }
}
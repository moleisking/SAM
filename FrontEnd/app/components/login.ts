import { Component } from "@angular/core";
import { LoginFormComponent } from "./login-form";

@Component({
    selector: "login-component",
    templateUrl: "/views/login.html",
    directives: [LoginFormComponent]
})

export class Login { }
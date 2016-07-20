import { Component } from "@angular/core";
import { ROUTER_DIRECTIVES } from "@angular/router";
import { LoginFormComponent } from "./login-form";

@Component({
    selector: "login-component",
    templateUrl: "/views/login.html",
    directives: [ROUTER_DIRECTIVES, LoginFormComponent]
})

export class Login { }
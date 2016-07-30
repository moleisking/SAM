import { Component } from "@angular/core";
import { ROUTER_DIRECTIVES } from "@angular/router";
import { RegisterFormComponent } from "./register-form";

@Component({
    selector: "register-component",
    templateUrl: "/views/register.html",
    directives: [ROUTER_DIRECTIVES, RegisterFormComponent]
})

export class Register { }
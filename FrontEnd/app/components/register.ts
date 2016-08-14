import { Component } from "@angular/core";
import { RegisterFormComponent } from "./register-form";

@Component({
    selector: "register-component",
    templateUrl: "/views/register.html",
    directives: [RegisterFormComponent]
})

export class Register { }
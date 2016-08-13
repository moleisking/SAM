import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { AuthService } from "../services/auth";
import { UserService } from "../services/user";

@Component({
    selector: "register-form-component",
    providers: [UserService, AuthService],
    templateUrl: "/views/register-form.html",
    styleUrls: ["/styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class RegisterFormComponent implements OnInit {

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;

    constructor(private auth: AuthService, private user: UserService, private router: Router, private formBuilder: FormBuilder) {
        this.message = "register messages here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            pass: ["", [<any>Validators.required, <any>Validators.minLength(5), <any>Validators.maxLength(20)]],
            email: ["", <any>Validators.required]
        });
    }

    register() {
        this.submitted = true;
        console.log(this.myForm.value);
        this.user.register(this.myForm.value).then(
            () => {
                this.router.navigate(["/login"]);
            },
            (res) => {
                this.message = res;
            }
        );
    }
}
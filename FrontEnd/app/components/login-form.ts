import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
// import { User } from "../models/user";

@Component({
    selector: "login-form-component",
    providers: [UserService, AuthService],
    templateUrl: "/views/login-form.html",
    styleUrls: ["/styles/login-form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class LoginFormComponent implements OnInit {

    public myForm: FormGroup; // our model driven form
    public submitted: boolean; // keep track on form submission
    // public events: any[] = []; // list of form changes
    public message: string;

    constructor(private auth: AuthService, private user: UserService, private router: Router, private formBuilder: FormBuilder) {
        this.message = "login messages here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            pass: ["", [<any>Validators.required, <any>Validators.minLength(5), <any>Validators.maxLength(20)]]
        });

        // this.subcribeToFormChanges();
    }

    // subcribeToFormChanges() {
    //     const myFormStatusChanges$ = this.myForm.statusChanges;
    //     const myFormValueChanges$ = this.myForm.valueChanges;

    //     myFormStatusChanges$.subscribe(x => this.events.push({ event: "STATUS_CHANGED", object: x }));
    //     myFormValueChanges$.subscribe(x => this.events.push({ event: "VALUE_CHANGED", object: x }));
    // }

    login() {
        this.submitted = true;
        console.log(this.myForm.value);
        this.auth.login(this.myForm.value).then(
            () => {
                this.router.navigate(["/dashboard"]);
            },
            (res) => {
                this.message = "invalid user";
            }
        )
    }
}
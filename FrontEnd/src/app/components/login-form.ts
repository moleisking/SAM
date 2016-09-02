import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { AuthService } from "../services/auth";
import { UserService } from "../services/user";

@Component({
    selector: "login-form-component",
    providers: [UserService, AuthService],
    templateUrl: "../../views/login-form.html",
    styleUrls: ["../../styles/form.css"],
    directives: [REACTIVE_FORM_DIRECTIVES]
})

export class LoginFormComponent implements OnInit {

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    // private events: any[] = []; // list of form changes
    private message: string;

    constructor(private auth: AuthService, private user: UserService, private router: Router, private formBuilder: FormBuilder) {
        this.message = "Login messages will come here.";
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
        this.message = "User sent to be logged in. Wait...";
        this.auth.login(this.myForm.value).subscribe(
            () => this.router.navigate(["/dashboard"]),
            error => this.message = "Login not possible." + error,
            () => console.log("Done login call.")
        );
    }
}
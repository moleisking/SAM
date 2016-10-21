import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { AuthService } from "../services/auth";

@Component({
    selector: "login-form-component",
    templateUrl: "../../views/login-form.html",
    styleUrls: ["../../styles/form.css"]
})

export class LoginFormComponent implements OnInit {

    private myForm: FormGroup;
    private message: string;

    constructor(
        private auth: AuthService,
        private router: Router,
        private formBuilder: FormBuilder
    ) {
        this.message = "Login messages will come here.";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            email: ["", Validators.required],
            passwords: this.formBuilder.group({
                pass: ["", [Validators.required, Validators.minLength(5)]]
            })
        });
    }

    login() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.message = "Form not valid to be sent.";
        else {
            this.message = "User sent to be logged in. Wait...";
            this.auth.login(this.myForm.value).subscribe(
                () => this.router.navigate(["/dashboard"]),
                error => this.message = "Login not possible." + error,
                () => console.log("Done login call.")
            );
        }
    }
}

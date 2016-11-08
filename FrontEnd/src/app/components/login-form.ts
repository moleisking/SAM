import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { AuthService } from "../services/auth";
import { TranslateService } from "ng2-translate";

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
        private formBuilder: FormBuilder,
        private trans: TranslateService
    ) {
        this.message = ""; // Login messages will come here.
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            email: ["", Validators.required],
            passwords: this.formBuilder.group({
                password: ["", [Validators.required, Validators.minLength(5)]]
            })
        });
    }

    login() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("UserSentLoggedIn").subscribe((res: string) => this.message = res);
            this.auth.login(this.myForm.value).subscribe(
                () => this.router.navigate(["/dashboard"]),
                error => this.trans.get("LoginNotPossible").subscribe((res: string) => this.message = res + error),
                () => this.trans.get("DoneContactUs").subscribe((res: string) => console.log(res))
            );
        }
    }
}

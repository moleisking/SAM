import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators } from "@angular/common";
import { UserService } from "../services/user";

@Component({
    selector: "forgottenpassword-component",
    templateUrl: "/views/forgottenpassword.html",
    styleUrls: ["/styles/forgottenpassword.css"],
    directives: [REACTIVE_FORM_DIRECTIVES],
    providers: [UserService]
})

export class ForgottenPassword implements OnInit {

    public myForm: FormGroup;
    message: string;

    constructor(private builder: FormBuilder, private formBuilder: FormBuilder, private user: UserService) {
        this.message = "Forgotten Password Text";
    }

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            email: ["", <any>Validators.required]
        });
    }

    send() {
        this.user.forgottenpassword(this.myForm.value)
            .subscribe(
            data => {
                this.message = data.message;
            },
            error => {
                this.message = "Error sending email.";
            },
            () => console.log("done forgotten password")
            );
    }
}
import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { UserService } from "../services/user";
import { TranslateService } from "ng2-translate";

@Component({
    selector: "forgottenpassword-form-component",
    templateUrl: "../../views/forgottenpassword-form.html",
    styleUrls: ["../../styles/form.css"]
})

export class ForgottenPasswordFormComponent implements OnInit {

    private myForm: FormGroup;
    private message: string;

    constructor(
        private builder: FormBuilder,
        private user: UserService,
        private trans: TranslateService
    ) {
        this.message = ""; // Forgotten Password Text
    }

    ngOnInit() {
        this.myForm = this.builder.group({
            email: ["", Validators.required]
        });
    }

    send() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
        else {
            this.user.forgottenpassword(this.myForm.value).subscribe(
                data => this.trans.get("PasswordSend").subscribe((res: string) => this.message = res),
                error => this.trans.get("ErrorSendingEmail").subscribe((res: string) => this.message = res),
                () => this.trans.get("DoneForgottenPassword").subscribe((res: string) => console.log(res))
            );
        }
    }
}

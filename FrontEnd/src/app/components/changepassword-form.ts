import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { UserService } from "../services/user";
import { TranslateService } from "ng2-translate";

import { ChangePasswordModel } from "../models/changepassword";

@Component({
    selector: "changepassword-form-component",
    templateUrl: "../../views/changepassword-form.html",
    styleUrls: ["../../styles/form.css"]
})

export class ChangePasswordFormComponent implements OnInit {

    private message: string;
    private model: ChangePasswordModel;
    private myForm: FormGroup;

    constructor(
        private user: UserService,
        private formBuilder: FormBuilder,
        private trans: TranslateService
    ) { this.message = ""; } // Profile form messages will be here.

    ngOnInit() {
        this.myForm = this.formBuilder.group({
            passwords: this.formBuilder.group({
                oldpassword: ["", [Validators.required, Validators.minLength(5)]],
                newpassword: ["", [Validators.required, Validators.minLength(5)]],
                confirmpassword: ["", [Validators.required, Validators.minLength(5)]]
            }, { validator: this.passsAreEqual }),
        });
    }

    passsAreEqual(group: FormGroup) {
        let valid = group.controls["newpassword"].value === group.controls["confirmpassword"].value;
        if (valid)
            return null;
        return { areEqual: true };
    }

    changePassword() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("ChangePasswordSent").subscribe((res: string) => this.message = res);
            this.user.changePassword(this.myForm.value.passwords).subscribe(
                () => this.trans.get("ChangePasswordSaved").subscribe((res: string) => this.message = res),
                error => this.message = <any>error,
                () => this.trans.get("DoneRegisterCall").subscribe((res: string) => console.log(res))
            );
        }
    }
}

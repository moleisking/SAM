import { Component, OnInit, OnDestroy } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { UserService } from "../services/user";
import { TranslateService } from "ng2-translate";

import { ChangePasswordModel } from "../models/changepassword";

@Component({
    selector: "forgottenpassword-component",
    templateUrl: "../../views/forgottenpassword.html",
    styleUrls: ["../../styles/form.css"]
})

export class ForgottenPasswordComponent implements OnInit, OnDestroy {

    private sub: any;
    private myForm: FormGroup;

    private guid: string;
    private message: string;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private formBuilder: FormBuilder,
        private user: UserService,
        private trans: TranslateService
    ) { }

    passsAreEqual(group: FormGroup) {
        let valid = group.controls["newpassword"].value === group.controls["confirmpassword"].value;
        if (valid)
            return null;
        return { areEqual: true };
    }

    ngOnInit() {
        this.sub = this.route.params.subscribe(p => {
            this.guid = p["id"];
            if (this.guid.length !== 32)
                this.router.navigate(["/"]);
            this.myForm = this.formBuilder.group({
                passwords: this.formBuilder.group({
                    newpassword: ["", [Validators.required, Validators.minLength(5)]],
                    confirmpassword: ["", [Validators.required, Validators.minLength(5)]]
                }, { validator: this.passsAreEqual }),
            });
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    changeForgottenPassword() {
        if (!this.myForm.dirty && !this.myForm.valid)
            this.trans.get("FormNotValid").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("ChangePasswordSent").subscribe((res: string) => this.message = res);
            let passwordForm: ChangePasswordModel = new ChangePasswordModel;
            passwordForm.oldpassword = this.guid;
            passwordForm.newpassword = this.myForm.value.passwords.newpassword;
            passwordForm.confirmpassword = this.myForm.value.passwords.confirmpassword;
            this.user.changeForgottenPassword(passwordForm).subscribe(
                () => this.router.navigate(["/login"]),
                error => this.message = <any>error,
                () => this.trans.get("DoneForgottenPassword").subscribe((res: string) => console.log(res))
            );
        }
    }
}

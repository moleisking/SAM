import { Component } from "@angular/core";
import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { Router } from "@angular/router";

@Component({
    selector: "login-component",
    templateUrl: "/views/login.html",
    providers: [UserService, AuthService]
})

export class Login {

    localUser = {
        name: "",
        pass: ""
    };

    message: string;

    constructor(private auth: AuthService, private user: UserService, private router: Router) {
        this.message = "login";
    }

    login() {
        this.auth.login(this.localUser).then(
            () => {
                this.router.navigate(["/dashboard"]);
            },
            (res) => {
                this.message = "invalid user";
            }
        )
    }

    register() {
        this.user.register(this.localUser).then(
            () => {
                this.message = "user registered";
            },
            (res) => {
                this.message = res;
            }
        );
    }
}
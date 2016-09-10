import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ROUTER_DIRECTIVES } from "@angular/router";

import { TAB_DIRECTIVES } from "ng2-tabs";
import { ProfileFormComponent } from "./profile-form";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";

import { UserModel } from "../models/user";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html",
    providers: [AuthService, UserService],
    directives: [ROUTER_DIRECTIVES, TAB_DIRECTIVES, ProfileFormComponent]
})

export class Dashboard implements OnInit {

    private message: string;
    private messageUsers: string;

    private users: UserModel[];

    constructor(private authService: AuthService, private user: UserService, private router: Router) {
        this.message = "My Dashboard in SAM";
    }

    ngOnInit() {
        this.getAllUsers();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => {
                this.users = users;
                // console.log(users);
            },
            error => this.messageUsers = <any>error,
            () => console.log("Done get all users.")
        );
    }
}

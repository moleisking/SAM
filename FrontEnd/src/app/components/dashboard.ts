import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { TAB_DIRECTIVES } from "ng2-tabs";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { MessageService } from "../services/message";

import { UserModel } from "../models/user";
import { MessageModel } from "../models/message";
import { ProfileModel } from "../models/profile";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html",
    directives: [TAB_DIRECTIVES]
})

export class Dashboard implements OnInit {

    private title: string;
    private errorUsers: string;
    private errorMessages: string;

    private usersList: UserModel[];
    private messagesList: MessageModel[];
    private model: ProfileModel;

    constructor(private authService: AuthService, private user: UserService, private router: Router,
        private messages: MessageService) {
        this.title = "My Dashboard in SAM";
    }

    ngOnInit() {
        this.getAllUsers();
        this.getAllMessages();
        this.user.getMyProfile().subscribe(
            profile => {
                this.model = profile;
                this.title = this.model.name;
            },
            error => this.errorUsers = <any>error,
            () => console.log("Done get my profile.")
        );
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }

    getAllUsers() {
        this.user.all().subscribe(
            users => this.usersList = users,
            error => this.errorUsers = <any>error,
            () => console.log("Done get all users.")
        );
    }

    getAllMessages() {
        this.messages.readAllLasts().subscribe(
            ml => {
                this.messagesList = ml;
                if (ml.length === 0)
                    this.errorMessages = "You have no messages yet.";
            },
            error => this.errorMessages = <any>error,
            () => console.log("Done get all messages.")
        );
    }
}

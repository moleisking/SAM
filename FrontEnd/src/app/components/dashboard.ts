import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { AuthService } from "../services/auth";
import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { CategoriesService } from "../services/categories";

import { UserModel } from "../models/user";
import { MessageModel } from "../models/message";
import { ProfileModel } from "../models/profile";
import { CategoryModel } from "../models/category";

@Component({
    selector: "dashboard-component",
    templateUrl: "../../views/dashboard.html"
})

export class Dashboard implements OnInit {

    private error: string;
    private errorUsers: string;
    private errorMessages: string;

    private usersList: UserModel[];
    private catList: CategoryModel[];
    private messagesList: MessageModel[];
    private model: ProfileModel = new ProfileModel();
    private modelProfileForm: ProfileModel;

    constructor(
        private authService: AuthService,
        private user: UserService,
        private router: Router,
        private messages: MessageService,
        private cat: CategoriesService
    ) { }

    ngOnInit() {
        this.getAllUsers();
        this.getCategories();
        this.getAllMessages();
        this.user.getMyProfile().subscribe(
            profile => {
                this.model = profile;
                this.modelProfileForm = profile;
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

    getCategories() {
        this.cat.all().subscribe(
            c => this.catList = c,
            error => this.error = <any>error,
            () => console.log("Done get all categories dashboard.")
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

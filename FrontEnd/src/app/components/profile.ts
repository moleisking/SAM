import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizer } from "@angular/platform-browser";

import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { RatingService } from "../services/rating";
import { AuthService } from "../services/auth";
import { CategoriesService } from "../services/categories";

import { ProfileModel } from "../models/profile";
import { MessageModel } from "../models/message";
import { RatingModel } from "../models/rating";
import { CategoryModel } from "../models/category";
import { UserDefaultImage } from "../config/userdefaultimage";

declare var jQuery: any;

@Component({
    selector: "profile-component",
    templateUrl: "../../views/profile.html",
    styleUrls: ["../../styles/profile.css"]
})

export class Profile implements OnInit, OnDestroy {

    private sub: any;
    private defaultImage = UserDefaultImage.image;
    private itsMe: boolean;

    private message: string;

    private model: ProfileModel = <ProfileModel>{};
    private cats: CategoryModel[];

    constructor(
        private route: ActivatedRoute,
        private user: UserService,
        private sanitizer: DomSanitizer,
        private m: MessageService,
        private r: RatingService,
        private authService: AuthService,
        private cat: CategoriesService
    ) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(p => {
            let id = p["id"];
            this.user.getProfile(id).subscribe(
                profile => {
                    this.model = profile;
                    this.model.image = profile.image === "" ? this.defaultImage : profile.image;
                    this.cat.all().subscribe(
                        c => {
                            this.cats = c;
                            this.model.categoryName = this.cats.find(x => x.id === this.model.category).name;
                            let tags: string = "";
                            this.model.tags.split(",").forEach(element => {
                                if (element)
                                    tags += this.cats.find(e => e.id === this.model.category).tags
                                        .find(e => e.id === +element).text + ", ";
                            });
                            this.model.tags = tags.substr(0, tags.length - 2);
                            if (this.authService.isLoggedIn()) {
                                this.user.getMyProfile().subscribe(
                                    my => this.itsMe = profile.email === my.email,
                                    error => this.message = <any>error,
                                    () => console.log("Done get my profile")
                                );
                                this.r.readProfileAuth(profile.nameurl).subscribe(
                                    prof => {
                                        this.model.rating = prof.myrating;
                                        this.model.average = prof.average;
                                    },
                                    error => this.message = <any>error,
                                    () => console.log("Done get rating profile")
                                );
                            }
                            else {
                                this.itsMe = true; // for unlogged we use same logic as it were us.
                                this.r.readProfile(profile.nameurl).subscribe(
                                    prof => this.model.average = prof.average,
                                    error => this.message = <any>error,
                                    () => console.log("Done get rating profile")
                                );
                            }
                        },
                        error => this.message = <any>error
                    );
                },
                error => this.message = <any>error,
                () => console.log("Done get profile")
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    rate() {
        if (this.model.rating < 0 || this.model.rating > 5)
            this.message = "Rating cant be more than 0 and less or equal to 5.";
        else {
            this.message = "Rating profile sent...";

            let model = new RatingModel();
            model.id = this.model.email;
            model.number = this.model.rating;

            this.r.add(model).subscribe(
                () => this.message = "Rating sent.",
                (res) => this.message = res,
                () => console.log("Done send rating")
            );
        }
    }

    sendMessage(messageText: string) {
        if (!messageText || !this.model.email || this.model.email === "undefined")
            this.message = "Message not valid to be sent.";
        else if (messageText.length < 4 || messageText.length > 500)
            this.message = "Message not cannot be less that 3 characters or bigger than 500.";
        else {
            this.message = "Message profile sent...";

            let model = new MessageModel();
            model.to = this.model.email;
            model.nameurl = this.model.nameurl;
            model.text = messageText;

            this.m.add(model).subscribe(
                () => this.message = "Message sent.",
                (res) => this.message = res,
                () => {
                    jQuery("#SendMessageModal").modal("hide");
                    console.log("Done send message");
                }
            );
        }
    }
}

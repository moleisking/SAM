import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { WorkService } from "../services/work";
import { WorkModel } from "../models/work";

@Component({
    selector: "work-component",
    templateUrl: "/views/work.html",
    providers: [WorkService]
})

export class Work implements OnInit, OnDestroy {

    private sub: any;
    private message: string;
    private workModel: WorkModel;

    constructor(private route: ActivatedRoute, private work: WorkService) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = params["id"];
            let username = params["username"];
            this.work.getWork(username, id).subscribe(
                    work => {
                        this.workModel = work;
                        console.log(this.workModel);
                    },
                error => this.message = <any>error);
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}
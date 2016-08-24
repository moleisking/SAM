import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { WorkService } from "../services/work";
import { WorkModel } from "../models/work";

@Component({
    selector: "work-component",
    templateUrl: "/views/work.html",
    providers: [WorkService]
})

export class Work implements OnInit {

    private sub: any;
    public message: string;
    public workModel: WorkModel;

    constructor(private route: ActivatedRoute, private work: WorkService) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = params["id"];
            this.work.getWork(id).subscribe(
                work => this.workModel = work,
                error => this.message = <any>error);
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}
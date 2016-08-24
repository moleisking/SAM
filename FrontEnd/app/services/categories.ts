import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Settings } from "../config/settings";
import { CategoryModel } from "../models/category";
import { ProfileModel } from "../models/profile";
import { Observable } from "rxjs/Rx";

@Injectable()

export class CategoriesService {

  constructor(private http: Http) { }

  all(): Observable<CategoryModel[]> {
    return this.http.get(Settings.backend_url + "/categories")
      .map(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    // console.log(res);
    let body = res.json();
    return body.data || {};
  }

  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}
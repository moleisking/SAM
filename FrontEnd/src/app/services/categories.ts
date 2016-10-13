import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { Settings } from "../config/settings";
import { CategoryModel } from "../models/category";
import { ProfileModel } from "../models/profile";

@Injectable()
export class CategoriesService {

  constructor(private http: Http) { }

  all(): Observable<CategoryModel[]> {
    return this.http.get(Settings.backend_url + "/categories")
      .map((res: Response) => res.json().categories)
      .catch(this.handleError);
  }

  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}

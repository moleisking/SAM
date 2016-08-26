import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Settings } from "../config/settings";
import { WorkModel } from "../models/work";
import { Observable } from "rxjs/Rx";

@Injectable()

export class WorkService {

  constructor(private http: Http) { }

  create(work): Observable<any> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/X-www-form-urlencoded");
    let c = "name=" + work.name + "&description=" + work.description + "&category=" + work.category + "&tags=" + work.tags;

    return this.http.post(Settings.backend_url + "/works/create", c, { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  allMyWorks(): Observable<WorkModel[]> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));

    return this.http.get(Settings.backend_url + "/works/allbyuser", { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  getWork(username, id): Observable<WorkModel> {
    return this.http.get(Settings.backend_url + "/works/" + username + "/" + id)
      .map(this.extractData).catch(this.handleError);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body.data || {};
  }

  private handleError(error: any) {
    // console.error(error);
    let errMsg = (error.message) ? error.message :
      (error._body) ? error._body :
        error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
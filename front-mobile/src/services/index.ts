import axios from "axios";

export const api = axios.create({
  baseURL: 'http://192.168.0.109:8080',
  // baseURL: 'http://192.168.0.8:8080',
  // baseURL: 'http://172.16.0.8:8080',
  // baseURL: 'http://192.168.0.109:8080',


  // baseURL: "https://kevin-dscatalog.herokuapp.com",
})

export const TOKEN = 'Basic ZHNjYXRhbG9nOmRzY2F0YWxvZzEyMw=='

export  function getProducts() {
  const res = api.get(`/products?page=0&linesPerPage=12&direction=ASC&orderBy=name`);
  return res;
}

export function getCategories(){
  const res = api.get(`/categories?direction=ASC&orderBy=name`);
  return res;
}
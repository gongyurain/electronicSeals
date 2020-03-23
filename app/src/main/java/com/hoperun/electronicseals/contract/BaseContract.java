package com.hoperun.electronicseals.contract;

public interface BaseContract {
    interface BaseView{
    }

    interface BaseModel{}

    abstract class BasePresenter <V extends BaseView,M extends BaseModel>{
        public V view;
        public M model;

        public BasePresenter(){
            this.model=creatModel();
        }
        public void attachView(V view){
            this.view=view;
        }

        public void detchView(){
            view=null;
        }

        protected abstract M creatModel();
    }
}

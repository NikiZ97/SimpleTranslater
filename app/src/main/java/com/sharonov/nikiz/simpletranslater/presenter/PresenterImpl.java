package com.sharonov.nikiz.simpletranslater.presenter;


import android.util.Log;

import com.sharonov.nikiz.simpletranslater.model.api.ApiInterface;
import com.sharonov.nikiz.simpletranslater.model.api.ApiModule;
import com.sharonov.nikiz.simpletranslater.model.data.LanguagesList;
import com.sharonov.nikiz.simpletranslater.ui.View;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class PresenterImpl implements Presenter {

    private View view;
    private ApiInterface apiInterface = ApiModule.providesApiInterface();
    private Subscription subscription = Subscriptions.empty();

    public PresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public void getLanguages() {

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        Observable<LanguagesList> listObs = apiInterface.getLanguageList(ApiInterface.API_KEY, "ru")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        listObs.subscribe(view::showLanguages, this::processError);

    }

    @Override
    public void getTranslate(String text, String languageTo) {
        apiInterface.getTranslate(ApiInterface.API_KEY, text, languageTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translatedText -> view.showTranslatedText(translatedText),
                        this::processError);
    }

    @Override
    public void onAddedToHistory() {

    }

    @Override
    public void onStarred() {

    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void processError(Throwable t) {
        Log.e("TAG", t.getLocalizedMessage(), t);
    }

}

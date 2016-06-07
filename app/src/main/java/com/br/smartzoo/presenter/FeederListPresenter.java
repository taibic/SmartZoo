package com.br.smartzoo.presenter;

import android.app.Activity;

import com.br.smartzoo.R;
import com.br.smartzoo.model.asynctask.DemitEmployeeAsyncTask;
import com.br.smartzoo.model.asynctask.LoadFeedersAsyncTask;
import com.br.smartzoo.model.asynctask.UpdateSalaryAsyncTask;
import com.br.smartzoo.model.entity.Employee;
import com.br.smartzoo.model.entity.Feeder;
import com.br.smartzoo.ui.activity.MainActivity;
import com.br.smartzoo.ui.view.FeederListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adenilson on 29/05/16.
 */
public class FeederListPresenter {

    private Activity mContext;
    private FeederListView mFeederListView;

    public FeederListPresenter(Activity context) {
        this.mContext = context;
    }

    public void attachView(FeederListView feederListView) {
        this.mFeederListView = feederListView;
    }

    public void detachView() {
        this.mFeederListView = null;
    }

    public void loadFeederList() {
        new LoadFeedersAsyncTask(mContext, new LoadFeedersAsyncTask.OnLoadFeedersList() {
            @Override
            public void onLoadFeederListSuccess(List<Feeder> feeders) {
                mFeederListView.onLoadFeederSuccess(feeders);
                ((MainActivity) mContext)
                        .showSnackBar(mContext.getString(R.string.messa_load_feeders_successful));
            }

            @Override
            public void onLoadFeedersListEmpty() {
                mFeederListView.onLoadFeederSuccess(new ArrayList<Feeder>());
                ((MainActivity) mContext)
                        .showSnackBar(mContext.getString(R.string.messa_load_feeders_empty));
            }

            @Override
            public void onLoadFeedersListFail() {
                ((MainActivity) mContext)
                        .showSnackBar(mContext.getString(R.string.messa_load_feeders_failed));
            }
        }).execute();
    }

    public void demitFeeder(Employee feeder) {
        new DemitEmployeeAsyncTask(mContext, new DemitEmployeeAsyncTask.OnDemit() {
            @Override
            public void onDemitSuccess() {
                ((MainActivity) mContext).showSnackBar(mContext.getString(R.string.message_demit));
                loadFeederList();
            }

            @Override
            public void onDemitFailed() {
                ((MainActivity) mContext)
                        .showSnackBar(mContext.getString(R.string.message_demit_failed));
            }
        }).execute(feeder.getId());
    }

    public void updateSalaryFeeder(Employee employee, Double value) {
        new UpdateSalaryAsyncTask(mContext, new UpdateSalaryAsyncTask.onSalaryChanged() {
            @Override
            public void onSalaryChangedSuccess() {
                ((MainActivity) mContext).showSnackBar(mContext
                        .getString(R.string.message_update_salary_success));
                loadFeederList();

            }

            @Override
            public void onSalaryChangedFailed() {
                ((MainActivity) mContext).showSnackBar(mContext
                        .getString(R.string.message_update_salary_failed));
            }
        }).execute(employee, value);
    }
}
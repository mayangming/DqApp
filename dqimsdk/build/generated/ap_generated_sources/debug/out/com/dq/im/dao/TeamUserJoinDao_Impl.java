package com.dq.im.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dq.im.model.TeamModel;
import com.dq.im.model.TeamUserJoinModel;
import com.dq.im.model.UserModel;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamUserJoinDao_Impl implements TeamUserJoinDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TeamUserJoinModel> __insertionAdapterOfTeamUserJoinModel;

  private final EntityInsertionAdapter<TeamUserJoinModel> __insertionAdapterOfTeamUserJoinModel_1;

  public TeamUserJoinDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamUserJoinModel = new EntityInsertionAdapter<TeamUserJoinModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `team_user_join` (`teamId`,`userId`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamUserJoinModel value) {
        if (value.getTeamId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTeamId());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserId());
        }
      }
    };
    this.__insertionAdapterOfTeamUserJoinModel_1 = new EntityInsertionAdapter<TeamUserJoinModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `team_user_join` (`teamId`,`userId`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamUserJoinModel value) {
        if (value.getTeamId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTeamId());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserId());
        }
      }
    };
  }

  @Override
  public void insert(final TeamUserJoinModel teamUserJoinModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamUserJoinModel.insert(teamUserJoinModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final List<TeamUserJoinModel> teamUserJoinModels) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamUserJoinModel_1.insert(teamUserJoinModels);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<TeamModel>> getTeamForUser(final String userId) {
    final String _sql = "SELECT * FROM team_table INNER JOIN team_user_join ON team_table.groupId = team_user_join.teamId WHERE team_user_join.userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"team_table","team_user_join"}, false, new Callable<List<TeamModel>>() {
      @Override
      public List<TeamModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "groupAvatarUrl");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfGroupNick = CursorUtil.getColumnIndexOrThrow(_cursor, "groupNick");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfUserId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<TeamModel> _result = new ArrayList<TeamModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TeamModel _item;
            _item = new TeamModel();
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _item.setUserId(_tmpUserId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            _item.setGroupId(_tmpGroupId);
            final String _tmpGroupAvatarUrl;
            _tmpGroupAvatarUrl = _cursor.getString(_cursorIndexOfGroupAvatarUrl);
            _item.setGroupAvatarUrl(_tmpGroupAvatarUrl);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            _item.setGroupName(_tmpGroupName);
            final String _tmpGroupNick;
            _tmpGroupNick = _cursor.getString(_cursorIndexOfGroupNick);
            _item.setGroupNick(_tmpGroupNick);
            final String _tmpCreateTime;
            _tmpCreateTime = _cursor.getString(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final String _tmpUserId_1;
            _tmpUserId_1 = _cursor.getString(_cursorIndexOfUserId_1);
            _item.setUserId(_tmpUserId_1);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<UserModel>> getUserForTeam(final String teamId) {
    final String _sql = "SELECT * FROM user_table INNER JOIN team_user_join ON user_table.userId = team_user_join.userId WHERE team_user_join.teamId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"user_table","team_user_join"}, false, new Callable<List<UserModel>>() {
      @Override
      public List<UserModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfNick = CursorUtil.getColumnIndexOrThrow(_cursor, "nick");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfPicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "picUrl");
          final int _cursorIndexOfIsFriend = CursorUtil.getColumnIndexOrThrow(_cursor, "isFriend");
          final int _cursorIndexOfUserId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<UserModel> _result = new ArrayList<UserModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final UserModel _item;
            _item = new UserModel();
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _item.setUserId(_tmpUserId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            _item.setUsername(_tmpUsername);
            final String _tmpNick;
            _tmpNick = _cursor.getString(_cursorIndexOfNick);
            _item.setNick(_tmpNick);
            final String _tmpSex;
            _tmpSex = _cursor.getString(_cursorIndexOfSex);
            _item.setSex(_tmpSex);
            final String _tmpPicUrl;
            _tmpPicUrl = _cursor.getString(_cursorIndexOfPicUrl);
            _item.setPicUrl(_tmpPicUrl);
            final String _tmpIsFriend;
            _tmpIsFriend = _cursor.getString(_cursorIndexOfIsFriend);
            _item.setIsFriend(_tmpIsFriend);
            final String _tmpUserId_1;
            _tmpUserId_1 = _cursor.getString(_cursorIndexOfUserId_1);
            _item.setUserId(_tmpUserId_1);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}

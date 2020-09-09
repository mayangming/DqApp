package com.dq.im.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dq.im.model.TeamModel;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TeamDao_Impl implements TeamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TeamModel> __insertionAdapterOfTeamModel;

  private final EntityDeletionOrUpdateAdapter<TeamModel> __updateAdapterOfTeamModel;

  private final SharedSQLiteStatement __preparedStmtOfUpdateForUserId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TeamDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTeamModel = new EntityInsertionAdapter<TeamModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `team_table` (`userId`,`groupId`,`groupAvatarUrl`,`groupName`,`groupNick`,`createTime`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamModel value) {
        if (value.getUserId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUserId());
        }
        if (value.getGroupId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getGroupId());
        }
        if (value.getGroupAvatarUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getGroupAvatarUrl());
        }
        if (value.getGroupName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getGroupName());
        }
        if (value.getGroupNick() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getGroupNick());
        }
        if (value.getCreateTime() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCreateTime());
        }
      }
    };
    this.__updateAdapterOfTeamModel = new EntityDeletionOrUpdateAdapter<TeamModel>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `team_table` SET `userId` = ?,`groupId` = ?,`groupAvatarUrl` = ?,`groupName` = ?,`groupNick` = ?,`createTime` = ? WHERE `groupId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TeamModel value) {
        if (value.getUserId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUserId());
        }
        if (value.getGroupId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getGroupId());
        }
        if (value.getGroupAvatarUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getGroupAvatarUrl());
        }
        if (value.getGroupName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getGroupName());
        }
        if (value.getGroupNick() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getGroupNick());
        }
        if (value.getCreateTime() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCreateTime());
        }
        if (value.getGroupId() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getGroupId());
        }
      }
    };
    this.__preparedStmtOfUpdateForUserId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE team_table SET groupName = ? WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM team_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(final TeamModel word) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamModel.insert(word);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final List<TeamModel> users) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTeamModel.insert(users);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TeamModel word) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTeamModel.handle(word);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateForUserId(final String id, final String name) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateForUserId.acquire();
    int _argIndex = 1;
    if (name == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, name);
    }
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateForUserId.release(_stmt);
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public int hasTeam(final String teamId) {
    final String _sql = "SELECT 1 FROM team_table WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<TeamModel>> getAllTeam() {
    final String _sql = "SELECT * from team_table ORDER BY groupId ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"team_table"}, false, new Callable<List<TeamModel>>() {
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
  public LiveData<TeamModel> getTeamByTeamId(final String teamId) {
    final String _sql = "SELECT * from team_table WHERE groupId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (teamId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teamId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"team_table"}, false, new Callable<TeamModel>() {
      @Override
      public TeamModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupAvatarUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "groupAvatarUrl");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfGroupNick = CursorUtil.getColumnIndexOrThrow(_cursor, "groupNick");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final TeamModel _result;
          if(_cursor.moveToFirst()) {
            _result = new TeamModel();
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _result.setUserId(_tmpUserId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            _result.setGroupId(_tmpGroupId);
            final String _tmpGroupAvatarUrl;
            _tmpGroupAvatarUrl = _cursor.getString(_cursorIndexOfGroupAvatarUrl);
            _result.setGroupAvatarUrl(_tmpGroupAvatarUrl);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            _result.setGroupName(_tmpGroupName);
            final String _tmpGroupNick;
            _tmpGroupNick = _cursor.getString(_cursorIndexOfGroupNick);
            _result.setGroupNick(_tmpGroupNick);
            final String _tmpCreateTime;
            _tmpCreateTime = _cursor.getString(_cursorIndexOfCreateTime);
            _result.setCreateTime(_tmpCreateTime);
          } else {
            _result = null;
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

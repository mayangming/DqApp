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
import com.dq.im.model.GroupTeamUserModel;
import com.dq.im.model.UserModel;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserModel> __insertionAdapterOfUserModel;

  private final EntityDeletionOrUpdateAdapter<UserModel> __updateAdapterOfUserModel;

  private final SharedSQLiteStatement __preparedStmtOfUpdateForUserId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserModel = new EntityInsertionAdapter<UserModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_table` (`userId`,`username`,`nick`,`sex`,`picUrl`,`isFriend`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserModel value) {
        if (value.getUserId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUserId());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUsername());
        }
        if (value.getNick() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNick());
        }
        if (value.getSex() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSex());
        }
        if (value.getPicUrl() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPicUrl());
        }
        if (value.getIsFriend() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getIsFriend());
        }
      }
    };
    this.__updateAdapterOfUserModel = new EntityDeletionOrUpdateAdapter<UserModel>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR IGNORE `user_table` SET `userId` = ?,`username` = ?,`nick` = ?,`sex` = ?,`picUrl` = ?,`isFriend` = ? WHERE `userId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserModel value) {
        if (value.getUserId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUserId());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUsername());
        }
        if (value.getNick() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNick());
        }
        if (value.getSex() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSex());
        }
        if (value.getPicUrl() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPicUrl());
        }
        if (value.getIsFriend() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getIsFriend());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getUserId());
        }
      }
    };
    this.__preparedStmtOfUpdateForUserId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE user_table SET userName = ? WHERE userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM user_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(final UserModel word) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserModel.insert(word);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final List<UserModel> users) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserModel.insert(users);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final UserModel userModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUserModel.handle(userModel);
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
  public int hasFriend(final String userId) {
    final String _sql = "SELECT 1 FROM user_table WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
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
  public LiveData<List<UserModel>> getAllUser() {
    final String _sql = "SELECT * from user_table WHERE isFriend = 1 ORDER BY userId ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"user_table"}, false, new Callable<List<UserModel>>() {
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
  public LiveData<List<GroupTeamUserModel>> getAllUserAboutCreateTeamForUser() {
    final String _sql = "SELECT * from user_table WHERE isFriend = 1 ORDER BY userId ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"user_table"}, false, new Callable<List<GroupTeamUserModel>>() {
      @Override
      public List<GroupTeamUserModel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfNick = CursorUtil.getColumnIndexOrThrow(_cursor, "nick");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfPicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "picUrl");
          final int _cursorIndexOfIsFriend = CursorUtil.getColumnIndexOrThrow(_cursor, "isFriend");
          final List<GroupTeamUserModel> _result = new ArrayList<GroupTeamUserModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final GroupTeamUserModel _item;
            final UserModel _tmpUserModel;
            if (! (_cursor.isNull(_cursorIndexOfUserId) && _cursor.isNull(_cursorIndexOfUsername) && _cursor.isNull(_cursorIndexOfNick) && _cursor.isNull(_cursorIndexOfSex) && _cursor.isNull(_cursorIndexOfPicUrl) && _cursor.isNull(_cursorIndexOfIsFriend))) {
              _tmpUserModel = new UserModel();
              final String _tmpUserId;
              _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
              _tmpUserModel.setUserId(_tmpUserId);
              final String _tmpUsername;
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
              _tmpUserModel.setUsername(_tmpUsername);
              final String _tmpNick;
              _tmpNick = _cursor.getString(_cursorIndexOfNick);
              _tmpUserModel.setNick(_tmpNick);
              final String _tmpSex;
              _tmpSex = _cursor.getString(_cursorIndexOfSex);
              _tmpUserModel.setSex(_tmpSex);
              final String _tmpPicUrl;
              _tmpPicUrl = _cursor.getString(_cursorIndexOfPicUrl);
              _tmpUserModel.setPicUrl(_tmpPicUrl);
              final String _tmpIsFriend;
              _tmpIsFriend = _cursor.getString(_cursorIndexOfIsFriend);
              _tmpUserModel.setIsFriend(_tmpIsFriend);
            }  else  {
              _tmpUserModel = null;
            }
            _item = new GroupTeamUserModel();
            _item.setUserModel(_tmpUserModel);
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
  public LiveData<UserModel> getUserByUserId(final String userId) {
    final String _sql = "SELECT * from user_table WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"user_table"}, false, new Callable<UserModel>() {
      @Override
      public UserModel call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfNick = CursorUtil.getColumnIndexOrThrow(_cursor, "nick");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfPicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "picUrl");
          final int _cursorIndexOfIsFriend = CursorUtil.getColumnIndexOrThrow(_cursor, "isFriend");
          final UserModel _result;
          if(_cursor.moveToFirst()) {
            _result = new UserModel();
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _result.setUserId(_tmpUserId);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            _result.setUsername(_tmpUsername);
            final String _tmpNick;
            _tmpNick = _cursor.getString(_cursorIndexOfNick);
            _result.setNick(_tmpNick);
            final String _tmpSex;
            _tmpSex = _cursor.getString(_cursorIndexOfSex);
            _result.setSex(_tmpSex);
            final String _tmpPicUrl;
            _tmpPicUrl = _cursor.getString(_cursorIndexOfPicUrl);
            _result.setPicUrl(_tmpPicUrl);
            final String _tmpIsFriend;
            _tmpIsFriend = _cursor.getString(_cursorIndexOfIsFriend);
            _result.setIsFriend(_tmpIsFriend);
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

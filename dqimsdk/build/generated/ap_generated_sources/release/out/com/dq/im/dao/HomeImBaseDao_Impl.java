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
import com.dq.im.model.HomeImBaseMode;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HomeImBaseDao_Impl extends HomeImBaseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HomeImBaseMode> __insertionAdapterOfHomeImBaseMode;

  private final EntityDeletionOrUpdateAdapter<HomeImBaseMode> __updateAdapterOfHomeImBaseMode;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTeFriendUnReadNumber;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTeamUnReadNumber;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAllUnReadNumber;

  private final SharedSQLiteStatement __preparedStmtOfUpdateHomeMessageByClientId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForServerMessageId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForFriendId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForGroupId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForUserId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForClientMessageId;

  public HomeImBaseDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHomeImBaseMode = new EntityInsertionAdapter<HomeImBaseMode>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `home_message` (`msgIdClient`,`msgIdServer`,`type`,`msgType`,`msgSecondType`,`createTime`,`sourceContent`,`messageSendStatus`,`toUserId`,`fromUserId`,`unReadNumber`,`groupId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, HomeImBaseMode value) {
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMsgIdServer());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        if (value.getMsgType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMsgType());
        }
        if (value.getMsgSecondType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getMsgSecondType());
        }
        stmt.bindLong(6, value.getCreateTime());
        if (value.getSourceContent() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSourceContent());
        }
        stmt.bindLong(8, value.getMessageSendStatus());
        if (value.getToUserId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getToUserId());
        }
        if (value.getFromUserId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getFromUserId());
        }
        stmt.bindLong(11, value.getUnReadNumber());
        if (value.getGroupId() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getGroupId());
        }
      }
    };
    this.__updateAdapterOfHomeImBaseMode = new EntityDeletionOrUpdateAdapter<HomeImBaseMode>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `home_message` SET `msgIdClient` = ?,`msgIdServer` = ?,`type` = ?,`msgType` = ?,`msgSecondType` = ?,`createTime` = ?,`sourceContent` = ?,`messageSendStatus` = ?,`toUserId` = ?,`fromUserId` = ?,`unReadNumber` = ?,`groupId` = ? WHERE `msgIdClient` = ? AND `msgIdServer` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, HomeImBaseMode value) {
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getMsgIdServer());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        if (value.getMsgType() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMsgType());
        }
        if (value.getMsgSecondType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getMsgSecondType());
        }
        stmt.bindLong(6, value.getCreateTime());
        if (value.getSourceContent() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getSourceContent());
        }
        stmt.bindLong(8, value.getMessageSendStatus());
        if (value.getToUserId() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getToUserId());
        }
        if (value.getFromUserId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getFromUserId());
        }
        stmt.bindLong(11, value.getUnReadNumber());
        if (value.getGroupId() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getGroupId());
        }
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getMsgIdServer());
        }
      }
    };
    this.__preparedStmtOfUpdateTeFriendUnReadNumber = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE home_message SET unReadNumber = ? WHERE fromUserId = ? OR toUserId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTeamUnReadNumber = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE home_message SET unReadNumber = ? WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAllUnReadNumber = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE home_message SET unReadNumber = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateHomeMessageByClientId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE home_message SET msgIdServer = ? ,messageSendStatus = ?,sourceContent = ? WHERE msgIdClient = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM home_message";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteForServerMessageId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM home_message WHERE msgIdServer = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteForFriendId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM home_message WHERE type = '1' AND (fromUserId = ? OR toUserId = ?)";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteForGroupId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM home_message WHERE groupId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteForUserId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM home_message WHERE fromUserId = ? OR toUserId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteForClientMessageId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM home_message WHERE msgIdClient = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final HomeImBaseMode homeImBaseMode) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHomeImBaseMode.insert(homeImBaseMode);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final List<HomeImBaseMode> homeImBaseModes) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfHomeImBaseMode.insert(homeImBaseModes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTeamUnReadNumber(final HomeImBaseMode homeImBaseMode) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfHomeImBaseMode.handle(homeImBaseMode);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateHomeMessage(final HomeImBaseMode homeImBaseMode, final boolean isAddUndReadNum,
      final String userId) {
    __db.beginTransaction();
    try {
      HomeImBaseDao_Impl.super.updateHomeMessage(homeImBaseMode, isAddUndReadNum, userId);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTeFriendUnReadNumber(final String friendId, final int unReadNum) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTeFriendUnReadNumber.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, unReadNum);
    _argIndex = 2;
    if (friendId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, friendId);
    }
    _argIndex = 3;
    if (friendId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, friendId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTeFriendUnReadNumber.release(_stmt);
    }
  }

  @Override
  public void updateTeamUnReadNumber(final String groupId, final int unReadNum) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTeamUnReadNumber.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, unReadNum);
    _argIndex = 2;
    if (groupId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, groupId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTeamUnReadNumber.release(_stmt);
    }
  }

  @Override
  public void updateAllUnReadNumber(final int unReadNum) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAllUnReadNumber.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, unReadNum);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateAllUnReadNumber.release(_stmt);
    }
  }

  @Override
  public void updateHomeMessageByClientId(final String clientId, final String serverId,
      final int messageSendStatus, final String sourceContent) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateHomeMessageByClientId.acquire();
    int _argIndex = 1;
    if (serverId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, serverId);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, messageSendStatus);
    _argIndex = 3;
    if (sourceContent == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, sourceContent);
    }
    _argIndex = 4;
    if (clientId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, clientId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateHomeMessageByClientId.release(_stmt);
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
  public void deleteForServerMessageId(final String messageId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForServerMessageId.acquire();
    int _argIndex = 1;
    if (messageId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, messageId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteForServerMessageId.release(_stmt);
    }
  }

  @Override
  public void deleteForFriendId(final String friendId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForFriendId.acquire();
    int _argIndex = 1;
    if (friendId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, friendId);
    }
    _argIndex = 2;
    if (friendId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, friendId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteForFriendId.release(_stmt);
    }
  }

  @Override
  public void deleteForGroupId(final String groupId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForGroupId.acquire();
    int _argIndex = 1;
    if (groupId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, groupId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteForGroupId.release(_stmt);
    }
  }

  @Override
  public void deleteForUserId(final String userId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForUserId.acquire();
    int _argIndex = 1;
    if (userId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (userId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteForUserId.release(_stmt);
    }
  }

  @Override
  public void deleteForClientMessageId(final String messageId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForClientMessageId.acquire();
    int _argIndex = 1;
    if (messageId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, messageId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteForClientMessageId.release(_stmt);
    }
  }

  @Override
  public int countHomeMessage() {
    final String _sql = "SELECT COUNT(*) FROM home_message";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public LiveData<List<HomeImBaseMode>> query() {
    final String _sql = "SELECT * FROM home_message ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"home_message"}, false, new Callable<List<HomeImBaseMode>>() {
      @Override
      public List<HomeImBaseMode> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
          final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
          final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
          final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
          final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
          final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
          final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
          final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
          final int _cursorIndexOfUnReadNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "unReadNumber");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final List<HomeImBaseMode> _result = new ArrayList<HomeImBaseMode>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final HomeImBaseMode _item;
            _item = new HomeImBaseMode();
            final String _tmpMsgIdClient;
            _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
            _item.setMsgIdClient(_tmpMsgIdClient);
            final String _tmpMsgIdServer;
            _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
            _item.setMsgIdServer(_tmpMsgIdServer);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            _item.setType(_tmpType);
            final String _tmpMsgType;
            _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
            _item.setMsgType(_tmpMsgType);
            final String _tmpMsgSecondType;
            _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
            _item.setMsgSecondType(_tmpMsgSecondType);
            final long _tmpCreateTime;
            _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
            _item.setCreateTime(_tmpCreateTime);
            final String _tmpSourceContent;
            _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
            _item.setSourceContent(_tmpSourceContent);
            final int _tmpMessageSendStatus;
            _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
            _item.setMessageSendStatus(_tmpMessageSendStatus);
            final String _tmpToUserId;
            _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
            _item.setToUserId(_tmpToUserId);
            final String _tmpFromUserId;
            _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
            _item.setFromUserId(_tmpFromUserId);
            final int _tmpUnReadNumber;
            _tmpUnReadNumber = _cursor.getInt(_cursorIndexOfUnReadNumber);
            _item.setUnReadNumber(_tmpUnReadNumber);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            _item.setGroupId(_tmpGroupId);
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
  public HomeImBaseMode queryHomeMessageModelByGroupId(final String groupId) {
    final String _sql = "SELECT * FROM home_message WHERE groupId = ? AND type = '2' ORDER BY createTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (groupId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, groupId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
      final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
      final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
      final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
      final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
      final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
      final int _cursorIndexOfUnReadNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "unReadNumber");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final HomeImBaseMode _result;
      if(_cursor.moveToFirst()) {
        _result = new HomeImBaseMode();
        final String _tmpMsgIdClient;
        _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
        _result.setMsgIdClient(_tmpMsgIdClient);
        final String _tmpMsgIdServer;
        _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
        _result.setMsgIdServer(_tmpMsgIdServer);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _result.setType(_tmpType);
        final String _tmpMsgType;
        _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
        _result.setMsgType(_tmpMsgType);
        final String _tmpMsgSecondType;
        _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
        _result.setMsgSecondType(_tmpMsgSecondType);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _result.setCreateTime(_tmpCreateTime);
        final String _tmpSourceContent;
        _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
        _result.setSourceContent(_tmpSourceContent);
        final int _tmpMessageSendStatus;
        _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
        _result.setMessageSendStatus(_tmpMessageSendStatus);
        final String _tmpToUserId;
        _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
        _result.setToUserId(_tmpToUserId);
        final String _tmpFromUserId;
        _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
        _result.setFromUserId(_tmpFromUserId);
        final int _tmpUnReadNumber;
        _tmpUnReadNumber = _cursor.getInt(_cursorIndexOfUnReadNumber);
        _result.setUnReadNumber(_tmpUnReadNumber);
        final String _tmpGroupId;
        _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
        _result.setGroupId(_tmpGroupId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public HomeImBaseMode queryHomeMessageModelByFriendId(final String friendId) {
    final String _sql = "SELECT * FROM home_message WHERE (fromUserId = ? OR toUserId = ?) AND type = '1' ORDER BY createTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (friendId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, friendId);
    }
    _argIndex = 2;
    if (friendId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, friendId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMsgIdClient = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdClient");
      final int _cursorIndexOfMsgIdServer = CursorUtil.getColumnIndexOrThrow(_cursor, "msgIdServer");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfMsgType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgType");
      final int _cursorIndexOfMsgSecondType = CursorUtil.getColumnIndexOrThrow(_cursor, "msgSecondType");
      final int _cursorIndexOfCreateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "createTime");
      final int _cursorIndexOfSourceContent = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceContent");
      final int _cursorIndexOfMessageSendStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageSendStatus");
      final int _cursorIndexOfToUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "toUserId");
      final int _cursorIndexOfFromUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "fromUserId");
      final int _cursorIndexOfUnReadNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "unReadNumber");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final HomeImBaseMode _result;
      if(_cursor.moveToFirst()) {
        _result = new HomeImBaseMode();
        final String _tmpMsgIdClient;
        _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
        _result.setMsgIdClient(_tmpMsgIdClient);
        final String _tmpMsgIdServer;
        _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
        _result.setMsgIdServer(_tmpMsgIdServer);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _result.setType(_tmpType);
        final String _tmpMsgType;
        _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
        _result.setMsgType(_tmpMsgType);
        final String _tmpMsgSecondType;
        _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
        _result.setMsgSecondType(_tmpMsgSecondType);
        final long _tmpCreateTime;
        _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
        _result.setCreateTime(_tmpCreateTime);
        final String _tmpSourceContent;
        _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
        _result.setSourceContent(_tmpSourceContent);
        final int _tmpMessageSendStatus;
        _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
        _result.setMessageSendStatus(_tmpMessageSendStatus);
        final String _tmpToUserId;
        _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
        _result.setToUserId(_tmpToUserId);
        final String _tmpFromUserId;
        _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
        _result.setFromUserId(_tmpFromUserId);
        final int _tmpUnReadNumber;
        _tmpUnReadNumber = _cursor.getInt(_cursorIndexOfUnReadNumber);
        _result.setUnReadNumber(_tmpUnReadNumber);
        final String _tmpGroupId;
        _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
        _result.setGroupId(_tmpGroupId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}

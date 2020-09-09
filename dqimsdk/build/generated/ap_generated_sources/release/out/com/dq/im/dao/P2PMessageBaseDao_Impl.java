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
import com.dq.im.model.ChatP2PModel;
import com.dq.im.model.P2PMessageBaseModel;
import com.dq.im.model.UserModel;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class P2PMessageBaseDao_Impl implements P2PMessageBaseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<P2PMessageBaseModel> __insertionAdapterOfP2PMessageBaseModel;

  private final EntityDeletionOrUpdateAdapter<P2PMessageBaseModel> __updateAdapterOfP2PMessageBaseModel;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessageForClientId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessageForFriendId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateP2PMessageByClientId;

  public P2PMessageBaseDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfP2PMessageBaseModel = new EntityInsertionAdapter<P2PMessageBaseModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `person_message` (`msgIdClient`,`msgIdServer`,`type`,`msgType`,`msgSecondType`,`createTime`,`sourceContent`,`messageSendStatus`,`toUserId`,`fromUserId`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, P2PMessageBaseModel value) {
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
      }
    };
    this.__updateAdapterOfP2PMessageBaseModel = new EntityDeletionOrUpdateAdapter<P2PMessageBaseModel>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `person_message` SET `msgIdClient` = ?,`msgIdServer` = ?,`type` = ?,`msgType` = ?,`msgSecondType` = ?,`createTime` = ?,`sourceContent` = ?,`messageSendStatus` = ?,`toUserId` = ?,`fromUserId` = ? WHERE `msgIdClient` = ? AND `msgIdServer` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, P2PMessageBaseModel value) {
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
        if (value.getMsgIdClient() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getMsgIdClient());
        }
        if (value.getMsgIdServer() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getMsgIdServer());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM person_message";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessageForClientId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM person_message WHERE msgIdClient = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMessageForFriendId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM person_message WHERE fromUserId = ? OR toUserId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateP2PMessageByClientId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE person_message SET msgIdServer = ? ,messageSendStatus = ?,sourceContent = ? WHERE msgIdClient = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final P2PMessageBaseModel p2PMessageBaseModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfP2PMessageBaseModel.insert(p2PMessageBaseModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final List<P2PMessageBaseModel> p2PMessageBaseModels) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfP2PMessageBaseModel.insert(p2PMessageBaseModels);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final P2PMessageBaseModel p2PMessageBaseModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfP2PMessageBaseModel.handle(p2PMessageBaseModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
  public void deleteMessageForClientId(final String msgClientId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessageForClientId.acquire();
    int _argIndex = 1;
    if (msgClientId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, msgClientId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMessageForClientId.release(_stmt);
    }
  }

  @Override
  public void deleteMessageForFriendId(final String friendId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessageForFriendId.acquire();
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
      __preparedStmtOfDeleteMessageForFriendId.release(_stmt);
    }
  }

  @Override
  public void updateP2PMessageByClientId(final String clientId, final String serverId,
      final int messageSendStatus, final String sourceContent) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateP2PMessageByClientId.acquire();
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
      __preparedStmtOfUpdateP2PMessageByClientId.release(_stmt);
    }
  }

  @Override
  public LiveData<List<P2PMessageBaseModel>> getAllMessage() {
    final String _sql = "SELECT * FROM person_message ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"person_message"}, false, new Callable<List<P2PMessageBaseModel>>() {
      @Override
      public List<P2PMessageBaseModel> call() throws Exception {
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
          final List<P2PMessageBaseModel> _result = new ArrayList<P2PMessageBaseModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final P2PMessageBaseModel _item;
            _item = new P2PMessageBaseModel();
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
  public LiveData<List<ChatP2PModel>> getAllChatMessage() {
    final String _sql = "SELECT * FROM person_message,user_table WHERE person_message.fromUserId = user_table.userId";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"person_message","user_table"}, false, new Callable<List<ChatP2PModel>>() {
      @Override
      public List<ChatP2PModel> call() throws Exception {
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
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfNick = CursorUtil.getColumnIndexOrThrow(_cursor, "nick");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfPicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "picUrl");
          final int _cursorIndexOfIsFriend = CursorUtil.getColumnIndexOrThrow(_cursor, "isFriend");
          final List<ChatP2PModel> _result = new ArrayList<ChatP2PModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ChatP2PModel _item;
            final P2PMessageBaseModel _tmpP2PMessageBaseModel;
            if (! (_cursor.isNull(_cursorIndexOfMsgIdClient) && _cursor.isNull(_cursorIndexOfMsgIdServer) && _cursor.isNull(_cursorIndexOfType) && _cursor.isNull(_cursorIndexOfMsgType) && _cursor.isNull(_cursorIndexOfMsgSecondType) && _cursor.isNull(_cursorIndexOfCreateTime) && _cursor.isNull(_cursorIndexOfSourceContent) && _cursor.isNull(_cursorIndexOfMessageSendStatus) && _cursor.isNull(_cursorIndexOfToUserId) && _cursor.isNull(_cursorIndexOfFromUserId))) {
              _tmpP2PMessageBaseModel = new P2PMessageBaseModel();
              final String _tmpMsgIdClient;
              _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
              _tmpP2PMessageBaseModel.setMsgIdClient(_tmpMsgIdClient);
              final String _tmpMsgIdServer;
              _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
              _tmpP2PMessageBaseModel.setMsgIdServer(_tmpMsgIdServer);
              final String _tmpType;
              _tmpType = _cursor.getString(_cursorIndexOfType);
              _tmpP2PMessageBaseModel.setType(_tmpType);
              final String _tmpMsgType;
              _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
              _tmpP2PMessageBaseModel.setMsgType(_tmpMsgType);
              final String _tmpMsgSecondType;
              _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
              _tmpP2PMessageBaseModel.setMsgSecondType(_tmpMsgSecondType);
              final long _tmpCreateTime;
              _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
              _tmpP2PMessageBaseModel.setCreateTime(_tmpCreateTime);
              final String _tmpSourceContent;
              _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
              _tmpP2PMessageBaseModel.setSourceContent(_tmpSourceContent);
              final int _tmpMessageSendStatus;
              _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
              _tmpP2PMessageBaseModel.setMessageSendStatus(_tmpMessageSendStatus);
              final String _tmpToUserId;
              _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
              _tmpP2PMessageBaseModel.setToUserId(_tmpToUserId);
              final String _tmpFromUserId;
              _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
              _tmpP2PMessageBaseModel.setFromUserId(_tmpFromUserId);
            }  else  {
              _tmpP2PMessageBaseModel = null;
            }
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
            _item = new ChatP2PModel();
            _item.setP2PMessageBaseModel(_tmpP2PMessageBaseModel);
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
  public LiveData<List<ChatP2PModel>> getAllChatMessage(final String userId) {
    final String _sql = "SELECT * FROM person_message,user_table WHERE (person_message.fromUserId = user_table.userId OR person_message.toUserId = user_table.userId) AND user_table.userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"person_message","user_table"}, false, new Callable<List<ChatP2PModel>>() {
      @Override
      public List<ChatP2PModel> call() throws Exception {
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
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfNick = CursorUtil.getColumnIndexOrThrow(_cursor, "nick");
          final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
          final int _cursorIndexOfPicUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "picUrl");
          final int _cursorIndexOfIsFriend = CursorUtil.getColumnIndexOrThrow(_cursor, "isFriend");
          final List<ChatP2PModel> _result = new ArrayList<ChatP2PModel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final ChatP2PModel _item;
            final P2PMessageBaseModel _tmpP2PMessageBaseModel;
            if (! (_cursor.isNull(_cursorIndexOfMsgIdClient) && _cursor.isNull(_cursorIndexOfMsgIdServer) && _cursor.isNull(_cursorIndexOfType) && _cursor.isNull(_cursorIndexOfMsgType) && _cursor.isNull(_cursorIndexOfMsgSecondType) && _cursor.isNull(_cursorIndexOfCreateTime) && _cursor.isNull(_cursorIndexOfSourceContent) && _cursor.isNull(_cursorIndexOfMessageSendStatus) && _cursor.isNull(_cursorIndexOfToUserId) && _cursor.isNull(_cursorIndexOfFromUserId))) {
              _tmpP2PMessageBaseModel = new P2PMessageBaseModel();
              final String _tmpMsgIdClient;
              _tmpMsgIdClient = _cursor.getString(_cursorIndexOfMsgIdClient);
              _tmpP2PMessageBaseModel.setMsgIdClient(_tmpMsgIdClient);
              final String _tmpMsgIdServer;
              _tmpMsgIdServer = _cursor.getString(_cursorIndexOfMsgIdServer);
              _tmpP2PMessageBaseModel.setMsgIdServer(_tmpMsgIdServer);
              final String _tmpType;
              _tmpType = _cursor.getString(_cursorIndexOfType);
              _tmpP2PMessageBaseModel.setType(_tmpType);
              final String _tmpMsgType;
              _tmpMsgType = _cursor.getString(_cursorIndexOfMsgType);
              _tmpP2PMessageBaseModel.setMsgType(_tmpMsgType);
              final String _tmpMsgSecondType;
              _tmpMsgSecondType = _cursor.getString(_cursorIndexOfMsgSecondType);
              _tmpP2PMessageBaseModel.setMsgSecondType(_tmpMsgSecondType);
              final long _tmpCreateTime;
              _tmpCreateTime = _cursor.getLong(_cursorIndexOfCreateTime);
              _tmpP2PMessageBaseModel.setCreateTime(_tmpCreateTime);
              final String _tmpSourceContent;
              _tmpSourceContent = _cursor.getString(_cursorIndexOfSourceContent);
              _tmpP2PMessageBaseModel.setSourceContent(_tmpSourceContent);
              final int _tmpMessageSendStatus;
              _tmpMessageSendStatus = _cursor.getInt(_cursorIndexOfMessageSendStatus);
              _tmpP2PMessageBaseModel.setMessageSendStatus(_tmpMessageSendStatus);
              final String _tmpToUserId;
              _tmpToUserId = _cursor.getString(_cursorIndexOfToUserId);
              _tmpP2PMessageBaseModel.setToUserId(_tmpToUserId);
              final String _tmpFromUserId;
              _tmpFromUserId = _cursor.getString(_cursorIndexOfFromUserId);
              _tmpP2PMessageBaseModel.setFromUserId(_tmpFromUserId);
            }  else  {
              _tmpP2PMessageBaseModel = null;
            }
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
            _item = new ChatP2PModel();
            _item.setP2PMessageBaseModel(_tmpP2PMessageBaseModel);
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
  public List<P2PMessageBaseModel> getMessageByFromIdAndToId(final String fromId,
      final int limitCount, final long createTime) {
    final String _sql = "SELECT * FROM person_message WHERE (person_message.fromUserId = ? OR person_message.toUserId = ?) AND person_message.createTime < ? ORDER BY createTime DESC LIMIT ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (fromId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fromId);
    }
    _argIndex = 2;
    if (fromId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fromId);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, createTime);
    _argIndex = 4;
    _statement.bindLong(_argIndex, limitCount);
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
      final List<P2PMessageBaseModel> _result = new ArrayList<P2PMessageBaseModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final P2PMessageBaseModel _item;
        _item = new P2PMessageBaseModel();
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
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<P2PMessageBaseModel> getMessageByFromIdAndToId(final String fromId,
      final int limitCount) {
    final String _sql = "SELECT * FROM person_message WHERE person_message.fromUserId = ? OR person_message.toUserId = ? LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (fromId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fromId);
    }
    _argIndex = 2;
    if (fromId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fromId);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, limitCount);
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
      final List<P2PMessageBaseModel> _result = new ArrayList<P2PMessageBaseModel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final P2PMessageBaseModel _item;
        _item = new P2PMessageBaseModel();
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
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}

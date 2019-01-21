import {StompConfig} from "@stomp/ng2-stompjs";

export const stompConfig: StompConfig = {
  url: 'ws://localhost:8080/FriendsBook/socket/websocket?token=Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMyIiwiZXhwIjoxNTQ4MjUxMTM5fQ.BCHntW1snXRDClwAhhAYig4LgHC518-ZUBrmcCL5EOkQIvvlMkQ4m-rOrtrxzj5-3vgvRxBtC4H00aZwp07Aog',
  headers: {},
  heartbeat_in: 0,
  heartbeat_out: 20000,
  reconnect_delay: 5000,
  debug: false
};

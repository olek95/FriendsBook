import {InjectableRxStompConfig, StompConfig} from "@stomp/ng2-stompjs";

export const stompConfig: InjectableRxStompConfig = <InjectableRxStompConfig>{
  brokerURL: 'ws://localhost:8080/FriendsBook/socket/websocket',
};

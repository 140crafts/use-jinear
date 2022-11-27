export default interface ModalState {
  visible: boolean;
}

export interface LoginWith2FaMailModalState extends ModalState {
  rerouteDisabled?: boolean;
}

export interface NotFoundModalState extends ModalState {
  imgSrc?: string;
  imgAlt?: string;
  title?: string;
  label?: string;
}

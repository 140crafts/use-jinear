export type UnionOfArrayElements<ARR_T extends Readonly<unknown[]>> =
  ARR_T[number];
export default UnionOfArrayElements;

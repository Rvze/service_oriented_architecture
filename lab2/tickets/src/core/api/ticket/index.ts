import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { IEvent, ISortTicketsType, ITicketType, ITicketVenueReqType } from './types/ITicketType.ts';
export const ticketApi = createApi({
    reducerPath: 'ticketApi',
    tagTypes: ['Ticket'],
    baseQuery: fetchBaseQuery({ baseUrl: 'http://158.160.57.219:8081/api/v1/' }),
    endpoints: (builder) => ({
        getTickets: builder.query<(ITicketType & IEvent)[], ISortTicketsType>({
            query: (data) => {
                if (data.id) {
                    return `tickets/${data.id}`;
                }
                if (data.page && data.page_size) {
                    return `tickets?page=${data.page}&page_size=${data.page_size}`;
                }
                return `tickets`;
            },
            providesTags: (result) =>
                result
                    ? [...result.map(({ id }) => ({ type: 'Ticket' as const, id })), { type: 'Ticket', id: 'LIST' }]
                    : [{ type: 'Ticket', id: 'LIST' }],
        }),
        addTicket: builder.mutation<ITicketType, Omit<ITicketType, 'id'>>({
            query: (body) => ({
                url: `tickets`,
                method: 'POST',
                body: {
                    ...body,
                    event: {
                        name: 'event',
                    },
                    creationDate: new Date(),
                },
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        deleteTicket: builder.mutation<void, number>({
            query: (id) => ({
                url: `tickets/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        updateTicketForm: builder.mutation<ITicketType, ITicketType>({
            query: (body) => ({
                url: `tickets/${body.id}`,
                method: 'PUT',
                body: {
                    ...body,
                    id: '',
                },
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        getVenueAmount: builder.mutation<number, ITicketVenueReqType>({
            query: (data) => ({
                url: `tickets/venue/amount?name=${data.name}&capacity=${data.capacity}&type=${data.type}`,
                method: 'GET',
            }),
        }),
        getUniquePrices: builder.mutation<number[], void>({
            query: () => ({
                url: `tickets/prices/unique`,
                method: 'GET',
            }),
        }),
        deleteByVenue: builder.mutation<void, ITicketVenueReqType>({
            query: (body) => ({
                url: `tickets/venue`,
                method: 'DELETE',
                body,
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        deleteEvent: builder.mutation<void, number>({
            query: (id) => ({
                url: `/booking/event/${id}/cancel`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
        deletePerson: builder.mutation<void, number>({
            query: (id) => ({
                url: `/booking/person/${id}/cancel`,
                method: 'DELETE',
            }),
            invalidatesTags: [{ type: 'Ticket', id: 'LIST' }],
        }),
    }),
});
export const {
    useGetTicketsQuery,
    useAddTicketMutation,
    useDeleteTicketMutation,
    useUpdateTicketFormMutation,
    useGetVenueAmountMutation,
    useGetUniquePricesMutation,
    useDeleteByVenueMutation,
    useDeleteEventMutation,
    useDeletePersonMutation,
} = ticketApi;
